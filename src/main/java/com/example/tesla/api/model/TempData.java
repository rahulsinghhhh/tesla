package com.example.tesla.api.model;

import com.example.tesla.exception.IncorrectFormattingException;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class TempData {
    private int deviceId;
    private long epochMs;
    private float temperature;

    public static TempData convert(@NonNull TempRequest tempRequest) {

        if (tempRequest == null || tempRequest.getData() == null) {
            throw new IncorrectFormattingException();
        }

        String[] dataElements = tempRequest.getData().split(":");

        if (dataElements.length != 4 || !"'Temperature'".equalsIgnoreCase(dataElements[2])) {
            throw new IncorrectFormattingException();
        }

        try {
            //Could be used to persist in the database
            return TempData.builder()
                    .deviceId(Integer.parseInt(dataElements[0]))
                    .epochMs(Long.parseLong(dataElements[1]))
                    .temperature(Float.parseFloat(dataElements[3]))
                    .build();
        } catch (Exception e) {
            throw new IncorrectFormattingException();
        }
    }


}
