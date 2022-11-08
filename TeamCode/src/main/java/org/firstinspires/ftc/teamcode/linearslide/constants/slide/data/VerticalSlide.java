package org.firstinspires.ftc.teamcode.linearslide.constants.slide.data;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.linearslide.constants.LinearConstants;
import org.firstinspires.ftc.teamcode.linearslide.constants.slide.LinearSlide;


@Config
public class VerticalSlide extends LinearSlide {

    public static PIDCoefficients coefficients = new PIDCoefficients(0.08, 0, 0.00007);

    public static int targetPosition = 0;

    public LinearConstants.VerticalSlideState state = LinearConstants.VerticalSlideState.HOME;

    // Expansion port 4
    public Servo claw;
    public Servo rotator;

    public VerticalSlide(HardwareMap hardwareMap) {
        super("verticalSlide", "verticalTouch", true);
        setCoefficients(coefficients);
        init(hardwareMap);
        claw = hardwareMap.get(Servo.class, "verticalClaw");
        rotator = hardwareMap.get(Servo.class, "rotator");
        rotator.setPosition(1);
        claw.setPosition(0);
    }

    public void openClaw() {
        claw.setPosition(1);
    }

    public void closeClaw() {
        claw.setPosition(0);
    }


    public void setState(LinearConstants.VerticalSlideState state) {
        this.state = state;
    }
}
