package org.firstinspires.ftc.teamcode.linearslide.constants.slide.data;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.linearslide.constants.slide.LinearSlide;


@Config
public class HorizontalSlide extends LinearSlide {

    public static PIDCoefficients coefficients = new PIDCoefficients(0.08, 0, 0.00007);

    public static int targetPosition = 0;

    // expansion port 3
    public Servo claw;

    public HorizontalSlide(HardwareMap hardwareMap) {
        super("horizontalSlide", "horizontalTouch");
        setCoefficients(coefficients);
        init(hardwareMap);
        claw = hardwareMap.get(Servo.class, "horizontalClaw");
        claw.setPosition(0);
    }

    public void openClaw() {
        claw.setPosition(1);
    }

    public void closeClaw() {
        claw.setPosition(0);
    }
}
