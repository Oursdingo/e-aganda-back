package com.e_aganda.demo.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Statut {
    EN_COURS("En cours"),
    TERMINEE("Terminée"),
    A_FAIRE("À faire");

    private final String label;

    Statut(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static Statut fromValue(String value) {
        for (Statut s : Statut.values()) {
            if (s.label.equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Statut inconnu: " + value);
    }
}
