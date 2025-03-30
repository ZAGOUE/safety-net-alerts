package com.safetynet.alerts.sandbox;

import com.safetynet.alerts.dto.FireDTO;
import com.safetynet.alerts.repository.JsonDataLoader;
import com.safetynet.alerts.service.FirestationService;

public class ManualTest {
    public static void main(String[] args) {
        FirestationService service = new FirestationService(new JsonDataLoader());
        FireDTO result = service.getFireInfoByAddress("1509 Culver St");
        System.out.println(result);
    }
}
