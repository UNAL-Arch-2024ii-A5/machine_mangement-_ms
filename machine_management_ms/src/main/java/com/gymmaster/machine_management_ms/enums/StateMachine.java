package com.gymmaster.machine_management_ms.enums;

public enum StateMachine {
    OCUPADA("Ocupada"),
    DISPONIBLE("Disponible"),
    EN_MANTENIMIENTO("En Mantenimiento");

    private final String state;

     StateMachine(String state) {
        this.state = state;
     }

    public String getState() {
        return state;
    }
}
