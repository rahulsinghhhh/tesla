package com.example.tesla.service;

import com.example.tesla.api.model.TempData;
import com.example.tesla.api.model.TempRequest;
import com.example.tesla.exception.IncorrectFormattingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.tesla.beans.TeslaConfiguration.LOCAL_ERROR_DB;

@Service
public class TempService {

    private static final int THRESHOLD_TEMPERATURE = 90;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd/ HH:mm:ss")
            .withZone(ZoneId.of("UTC"));
    @NonNull
    private final ObjectMapper objectMapper;
    @NonNull
    private final List<String> errorTempList;

    @Autowired
    public TempService(ObjectMapper objectMapper, @Qualifier(LOCAL_ERROR_DB) List<String> localErrorDb) {
        this.errorTempList = localErrorDb;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> addTempData(@NonNull final TempRequest tempRequest) throws IncorrectFormattingException {
        Map<String, Object> returnMap = new HashMap<>();

        TempData tempObj;
        try {
            tempObj = TempData.convert(tempRequest);
        } catch (IncorrectFormattingException incorrectFormattingException) {
            errorTempList.add(tempRequest.getData());
            throw incorrectFormattingException;
        }

        returnMap.put("overtemp", false);

        if (tempObj.getTemperature() >= THRESHOLD_TEMPERATURE) {
            Instant instant = Instant.ofEpochMilli(tempObj.getEpochMs());
            returnMap.put("overtemp", true);
            returnMap.put("device_id", tempObj.getDeviceId());
            returnMap.put("formatted_time", DATE_TIME_FORMATTER.format(instant));
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
}
