package com.example.tesla.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Temp {
    private int deviceId;
    private long epochMs;
    private float temperature;

    public Temp(int deviceId, long epochMs, float temperature) {
        this.deviceId = deviceId;
        this.epochMs = epochMs;
        this.temperature = temperature;
    }

}
