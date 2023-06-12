package com.example.tesla.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Builder
@Data
@Jacksonized
public class TempData {
    private int deviceId;
    private long epochMs;
    private float temperature;

}
