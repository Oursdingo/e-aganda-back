package com.e_aganda.demo.error;

public class ProjetAlreadyExists extends RuntimeException {
    public ProjetAlreadyExists(String message) {
        super("Project with "+message+" Already exist !!!");
    }
}
