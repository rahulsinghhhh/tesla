package com.example.tesla.service;

import com.example.tesla.api.model.Temp;
import com.example.tesla.exception.IncorrectFormattingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TempService {

    @Autowired
    ObjectMapper objectMapper;
    private List<String> errorTempList;

    private List<String> tempList;

    public TempService() {
        errorTempList = new ArrayList<>();
        tempList = new ArrayList<>();
    }

    public Map addTempData(@NonNull String temp) throws IncorrectFormattingException{

        Map<String, Object> returnMap = new HashMap<>();

        if (!validateTemp(temp)) {
            errorTempList.add(temp);
            throw new IncorrectFormattingException();
        }

        Map<String, String> map;
        String dataElements[] = new String[4];
        try {
            map = objectMapper.readValue(temp, Map.class);
            dataElements = map.get("data").split(":");
        } catch (Exception e) {
        }

        Temp tempObj = Temp.builder()
                .deviceId(Integer.parseInt(dataElements[0]))
                .epochMs(Long.parseLong(dataElements[1]))
                .temperature(Float.parseFloat(dataElements[3]))
                .build();

        returnMap.put("overtemp", false);

        if (tempObj.getTemperature()>=90) {
            returnMap.put("overtemp", true);
            returnMap.put("device_id", tempObj.getDeviceId());
            returnMap.put("formatted_time", tempObj.getEpochMs());
        }

        return returnMap;
    }

    public Map getTempErrors() {
        Map<String, List> map = new HashMap<>();
        map.put("errors", errorTempList);
        return map;
    }

    public void clearErrorList() {
        errorTempList = new ArrayList<>();
    }
    private boolean validateTemp(String jsonStr) {
        if (jsonStr==null) return false;

        Map<String, String> map;

        try {
            map = objectMapper.readValue(jsonStr, Map.class);
            if (map==null || map.get("data")==null) return false;
        } catch (Exception e) {
            return false;
        }

        String data = map.get("data");

        String dataElements[] = data.split(":");
        if (dataElements.length!=4) return false;

        try {
            Integer.parseInt(dataElements[0]);
            Long.parseLong(dataElements[1]);
            if (!dataElements[2].toString().equalsIgnoreCase("'Temperature'")) return false;
            Float.parseFloat(dataElements[3]);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
