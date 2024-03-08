package com.eximbills.tlsdemo;

import java.security.cert.X509Certificate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcomewelcome(@RequestParam(required = false, value = "name") String name) {
        return "welcome";
    };

    @PostMapping("/csexim")
    public String csexim(@RequestBody String lcBody, HttpServletRequest request) {
        X509Certificate[] certificates = (X509Certificate[]) request
                .getAttribute("jakarta.servlet.request.X509Certificate");
        if (certificates != null && certificates.length > 0) {
            lcBody = lcBody + " " + certificates[0].getSubjectX500Principal().getName();
        }
        return lcBody;
    };

}
