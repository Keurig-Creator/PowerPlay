package org.firstinspires.ftc.teamcode.linearslide.state;

public enum SlideMotorState {

    UNKNOWN("Unknown"),
    INITIALIZED("Initialized"),
    TIMEOUT("Timed out"),
    NO_ENCODER("Encoder not found"),
    NO_SWITCH("Switch not found");

    SlideMotorState(String error) {

    }

}
