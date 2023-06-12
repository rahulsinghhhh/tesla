package com.example.tesla.api.controller;

import com.example.tesla.service.TempService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TempController {

    @Autowired
    TempService tempService;

    @PostMapping(value="/temp",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Map addTempData(@RequestBody@NonNull String temp) {
        return tempService.addTempData(temp);
    }
    @GetMapping("/errors")
    public Map getTempErrors() {
        return tempService.getTempErrors();
    }

    @DeleteMapping("/errors")
    public void deleteTempErrors() {
        tempService.clearErrorList();
    }
}
