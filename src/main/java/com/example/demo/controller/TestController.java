package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;
import org.checkerframework.framework.qual.TargetLocations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test") //리소스
public class TestController {
    @GetMapping("/testGetMapping")
    public String testControllerWithPath(){
        return "Hello World! testGetMapping";
    }

    @GetMapping("/{id}")
    public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
        return "Hello World! ID " + id;
    }
    @GetMapping("/testRequestParam")
    public String testControllerRequestParam(@RequestParam(required = false) int id) {
        return "Hello World! ID " + id;
    }

    @GetMapping("/testRequestBody")
    public ResponseDTO<String> testControllerResponseBody() {
        List<String> list = new ArrayList<>();
        list.add("Hello World! I'm ResponseDTO");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return response;
    }
    
    @GetMapping("/testRequestEntity")
    public ResponseEntity<?> testControllerResponseEnity() {
        List<String> list = new ArrayList<>();
        list.add("Hello World! I'm ResponseEntity. And you got 404");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        //http status를 400으로 설정
        return ResponseEntity.badRequest().body(response);
    }
}
