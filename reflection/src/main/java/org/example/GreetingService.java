package org.example;


public class GreetingService {
    public void hello() {
        System.out.println("Hello");
    }

    @LogInvocation
    public void gloryToUkraine() {
        System.out.println("Glory To Ukraine");
    }
}
