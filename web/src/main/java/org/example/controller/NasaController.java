package org.example.controller;

import org.example.service.NasaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/pictures")
public class NasaController {

    final NasaService nasaService;

    public NasaController(NasaService nasaService) {
        this.nasaService = nasaService;
    }

    @GetMapping(value = "/{sol}/largest")
    public ResponseEntity<String> getLargestImage(@PathVariable("sol") int sol) {
        try {
            return ResponseEntity
                    .status(HttpStatus.PERMANENT_REDIRECT)
                    .location(new URI(nasaService.getLargestImage(sol)))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
