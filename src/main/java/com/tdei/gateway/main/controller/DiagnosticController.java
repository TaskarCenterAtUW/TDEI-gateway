package com.tdei.gateway.main.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiagnosticController {

    @Hidden
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, path = "/health/ping")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("I'm healthy !");
    }
}

