package org.firstinspires.ftc.teamcode.linearslide.slide.data;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.linearslide.constants.LinearConstants;
import org.firstinspires.ftc.teamcode.linearslide.slide.LinearSlide;


@Config
public class HorizontalSlide extends LinearSlide {

    public static PIDCoefficients coefficients = new PIDCoefficients(0.08, 0, 0.0008);

    public static int targetPosition = 0;

    public LinearConstants.HorizontalSlideState state = LinearConstants.HorizontalSlideState.HOME;

    public HorizontalSlide(HardwareMap hardwareMap) {
        super("horizontalSlide", "horizontalTouch");
        setCoefficients(coefficients);
        init(hardwareMap);
    }


    public void setState(LinearConstants.HorizontalSlideState state) {
        this.state = state;
    }
}
