package org.firstinspires.ftc.teamcode.linearslide.slide.data;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.linearslide.constants.LinearConstants;
import org.firstinspires.ftc.teamcode.linearslide.slide.LinearSlide;


@Config
public class VerticalSlide extends LinearSlide {

    public static PIDCoefficients coefficients = new PIDCoefficients(0.05, 0, 0.00092);

    public static int targetPosition = 0;

    public LinearConstants.VerticalSlideState state = LinearConstants.VerticalSlideState.HOME;

    public VerticalSlide(HardwareMap hardwareMap) {
        super("verticalSlide", "verticalTouch", true);
        setCoefficients(coefficients);
        init(hardwareMap);
    }


    public void setState(LinearConstants.VerticalSlideState state) {
        this.state = state;
    }
}
