package org.example.models;


public enum Role {
    USER("USER"), ADMIN("ADMIN");

    public String ROLE;

    Role(String role) {
        this.ROLE = role;
    }
}