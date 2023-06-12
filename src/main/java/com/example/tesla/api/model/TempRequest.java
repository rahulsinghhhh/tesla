package com.example.tesla.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
public class TempRequest {
    private String data;
}
