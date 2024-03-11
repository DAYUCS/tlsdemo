package com.eximbills.tlsdemo;

import java.security.cert.X509Certificate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class WelcomeController {

    private static final String WELCOME_URL = "https://localhost/welcome";

    @Autowired
    private RestTemplate restTemplate;

    // Access another https service with this server's certificate
    @GetMapping("/welcomeclient")
    public String greetMessage() {
        String response = restTemplate.getForObject(WELCOME_URL, String.class);
        return response;
    }

    // Https GET service
    @GetMapping("/welcome")
    public String welcomewelcome(@RequestParam(required = false, value = "name") String name,
            HttpServletRequest request) {
        X509Certificate[] certificates = (X509Certificate[]) request
                .getAttribute("jakarta.servlet.request.X509Certificate");
        if (certificates != null && certificates.length > 0) {
            name = name + " with cert " + certificates[0].getSubjectX500Principal().getName();
        }
        return "welcome " + name;
    };

    // Https POST service
    @PostMapping("/csexim")
    public String csexim(@RequestBody String lcBody, HttpServletRequest request) {
        X509Certificate[] certificates = (X509Certificate[]) request
                .getAttribute("jakarta.servlet.request.X509Certificate");
        if (certificates != null && certificates.length > 0) {
            System.out.println(certificates[0].getSubjectX500Principal().getName());
        }
        System.out.println(lcBody);
        return lcBody;
    };

}
