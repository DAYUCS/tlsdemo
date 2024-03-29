package com.eximbills.tlsdemo;

import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class WelcomeController {

    private static final String WELCOME_URL = "https://localhost:8443/welcome?name={name}";

    @Autowired
    private RestTemplate restTemplate;

    // Access another https service with this server's certificate
    @GetMapping("/welcomeclient")
    public String greetMessage(@RequestParam(required = false, value = "name") String name,
            HttpServletRequest request, @AuthenticationPrincipal Jwt jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt.getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        Map<String, String> params = Collections.singletonMap("name", name);
        String response = restTemplate.exchange(WELCOME_URL, HttpMethod.GET, entity, String.class, params).getBody();
        return response;
    }

    // Https GET service
    @GetMapping("/welcome")
    public String welcomewelcome(@RequestParam(required = false, value = "name") String name,
            HttpServletRequest request, @AuthenticationPrincipal Jwt jwt) {
        X509Certificate[] certificates = (X509Certificate[]) request
                .getAttribute("jakarta.servlet.request.X509Certificate");
        if (certificates != null && certificates.length > 0) {
            name = name + " with cert " + certificates[0].getSubjectX500Principal().getName();
        }
        return "welcome " + name + " with jwt " + jwt.getClaimAsString("preferred_username");
    };

    // Https POST service
    @PostMapping("/csexim")
    public String csexim(@RequestBody String lcBody, HttpServletRequest request, @AuthenticationPrincipal Jwt jwt) {
        X509Certificate[] certificates = (X509Certificate[]) request
                .getAttribute("jakarta.servlet.request.X509Certificate");
        if (certificates != null && certificates.length > 0) {
            System.out.println(certificates[0].getSubjectX500Principal().getName());
        }
        System.out.println(lcBody);
        System.out.println(jwt.getClaimAsString("preferred_username"));
        return lcBody;
    };

}
