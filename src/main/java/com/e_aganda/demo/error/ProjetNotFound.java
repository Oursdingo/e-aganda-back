package com.e_aganda.demo.error;

public class ProjetNotFound extends RuntimeException {
    public ProjetNotFound(String message) {
        super("Projet with this specific name :"+message+"not found");
    }
}
