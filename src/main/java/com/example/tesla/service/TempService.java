package com.example.tesla.service;

import com.example.tesla.api.model.TempData;
import com.example.tesla.exception.IncorrectFormattingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.tesla.beans.TeslaConfiguration.LOCAL_ERROR_DB;

@Service
public class TempService {

    @NonNull
    private final ObjectMapper objectMapper;
    @NonNull
    private final List<String> errorTempList;

    @Autowired
    public TempService(final ObjectMapper objectMapper, @Qualifier(LOCAL_ERROR_DB) List<String> localErrorDb) {
        this.errorTempList = localErrorDb;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> addTempData(@NonNull final String tempData) throws IncorrectFormattingException{

        Map<String, Object> returnMap = new HashMap<>();

        if (!validateTemp(tempData)) {
            errorTempList.add(tempData);
            throw new IncorrectFormattingException();
        }

        Map<String, String> map;
        String[] dataElements;
        try {
            map = objectMapper.readValue(tempData, Map.class);
            dataElements = map.get("data").split(":");
        } catch (Exception e) {
            throw new IncorrectFormattingException();
        }

        //Could be used to persist in the database
        TempData tempObj = TempData.builder()
                .deviceId(Integer.parseInt(dataElements[0]))
                .epochMs(Long.parseLong(dataElements[1]))
                .temperature(Float.parseFloat(dataElements[3]))
                .build();

        returnMap.put("overtemp", false);

        if (tempObj.getTemperature() >= 90) {
            returnMap.put("overtemp", true);
            returnMap.put("device_id", tempObj.getDeviceId());
            returnMap.put("formatted_time", tempObj.getEpochMs());
        }

        return returnMap;
    }

    public Map<String, List<String>> getTempErrors() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("errors", errorTempList);
        return map;
    }

    public void clearErrorList() {
        errorTempList.clear();
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

        String[] dataElements = data.split(":");
        if (dataElements.length!=4) return false;

        try {
            Integer.parseInt(dataElements[0]);
            Long.parseLong(dataElements[1]);
            if (!dataElements[2].equalsIgnoreCase("'Temperature'")) return false;
            Float.parseFloat(dataElements[3]);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
