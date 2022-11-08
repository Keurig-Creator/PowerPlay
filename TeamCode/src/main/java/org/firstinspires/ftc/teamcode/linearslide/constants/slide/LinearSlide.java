package org.firstinspires.ftc.teamcode.linearslide.constants.slide;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.linearslide.constants.LinearConstants;
import org.firstinspires.ftc.teamcode.linearslide.state.SlideMotorState;
import org.firstinspires.ftc.teamcode.pid.PIDWrapper;

@Config
public class LinearSlide extends PIDWrapper {

    private DcMotorEx motor;
    private TouchSensor sensor;

    private final String motorName;
    private final String sensorName;

    private PIDCoefficients coefficients = new PIDCoefficients();

    private final ElapsedTime runtime = new ElapsedTime();

    // Checks if motor homed or not
    private boolean motorTimeout = false;

    // Disable home if past seconds
    public static int homeTimer = 3;

    // Switch timer to detect if switch is active
    public static double switchTimer = 0.3;

    // Disable motor usage if motor state is not Initialized
    public SlideMotorState slideMotorState = SlideMotorState.UNKNOWN;

    private boolean reverseMotor;


    public enum Mode {
        AUTO,
        MANUEL
    }

    public Mode mode = Mode.AUTO;

    public LinearSlide(String motorName, String sensorName) {
        this(motorName, sensorName, false);
    }

    public LinearSlide(String motorName, String sensorName, boolean reverseMotor) {
        this.motorName = motorName;
        this.sensorName = sensorName;
        this.reverseMotor = reverseMotor;
    }

    protected void init(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, motorName);
        sensor = hardwareMap.get(TouchSensor.class, sensorName);

        // Change motor direction if needed
        motor.setDirection(reverseMotor ? DcMotorSimple.Direction.REVERSE : DcMotorSimple.Direction.FORWARD);

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        setMotor(motor);
    }

    public void extend(double power) {

        if (motor.getCurrentPosition() >= 1100) {
            motor.setPower(0);
            return;
        }
        motor.setPower(power);
    }

    public void retract(double power) {

        if (motor.getCurrentPosition() <= 0) {
            motor.setPower(0);
            return;
        }

        if (motor.getCurrentPosition() <= 500) {
            motor.setPower(-0.5);
            return;
        }

        motor.setPower(-power);
    }

    public void retractCheck() {
        if (mode == Mode.MANUEL) {
            if (motor.getCurrentPosition() <= 0) {
                double power = getPID(0, motor.getCurrentPosition());
                motor.setPower(power);
            }
        }
    }

    /**
     * Home motor using touch sensor with safety checks
     */
    public void home() {
        runtime.reset();

        // Move forward if touching sensor
        while (!sensor.isPressed()) {

            // Timeout and state for no switch found
            if (runtime.seconds() >= switchTimer) {
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                slideMotorState = SlideMotorState.NO_SWITCH;
                return;
            }

            motor.setPower(LinearConstants.HOME_SPEED);
        }

        motor.setPower(0);

        // Start position
        int currentPosition = motor.getCurrentPosition();

        runtime.reset();

        while (runtime.seconds() < homeTimer) {
            if (sensor.isPressed()) {
                motor.setPower(-LinearConstants.HOME_SPEED);
            } else {
                motor.setPower(0);
            }

            // Check if both motors are at the home position
            if (!sensor.isPressed()) {
                motorTimeout = true;
                break;
            }

        }

        // End Position
        int stopPosition = motor.getCurrentPosition();

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Motor safety check
        if (currentPosition == stopPosition) {
            slideMotorState = SlideMotorState.NO_ENCODER;
            return;
        } else if (!motorTimeout) {
            slideMotorState = SlideMotorState.TIMEOUT;
            return;
        } else {
            slideMotorState = SlideMotorState.INITIALIZED;
        }

        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        runtime.reset();

//        while (runtime.seconds() <= 0.2) {
//            double power = getPID(0, motor.getCurrentPosition());
//            motor.setPower(power);
//        }

        motor.setPower(0);
    }

    public DcMotorEx getMotor() {
        return motor;
    }

    public TouchSensor getSensor() {
        return sensor;
    }
}
