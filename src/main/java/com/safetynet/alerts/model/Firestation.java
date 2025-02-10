package com.safetynet.alerts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // ✅ Constructeur vide requis par Jackson
@AllArgsConstructor // ✅ Constructeur avec paramètres pour les tests
public class Firestation {
    private String address;
    private int station;
}
