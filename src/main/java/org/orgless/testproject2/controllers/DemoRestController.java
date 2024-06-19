package org.orgless.testproject2.controllers;

import lombok.RequiredArgsConstructor;
import org.orgless.testproject2.service.DemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DemoRestController {
    private final DemoService service;

    @GetMapping("index")
    public ResponseEntity<Map<String, String>> getMap(@RequestBody List<String> userIds) {
        return ResponseEntity.ok(
                service.getMap(userIds)
        );
    }
}
