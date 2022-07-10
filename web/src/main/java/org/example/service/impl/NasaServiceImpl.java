package org.example.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.example.service.NasaService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Cacheable("largestImage")
public class NasaServiceImpl implements NasaService {

    @SneakyThrows
    @Override
    public String getLargestImage(int sol) {
        var restTemplate = new RestTemplate();
        var factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build());
        restTemplate.setRequestFactory(factory);

        var url = UriComponentsBuilder.fromHttpUrl("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos")
                .queryParam("sol", sol)
                .queryParam("api_key", API_KEY)
                .build().toUri();

        var images = Objects.requireNonNull(restTemplate.getForObject(url, JsonNode.class)).findValues("img_src");

        return images
                .stream()
                .collect(Collectors.toMap(
                                imgSrc -> imgSrc, imgScr -> {
                                    long size = 0;
                                    try {
                                        size = restTemplate.headForHeaders(new URI(imgScr.asText())).getContentLength();
                                    } catch (URISyntaxException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return size;
                                }
                        )
                ).entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()).get().getKey().asText();
    }
}
