package org.firstinspires.ftc.teamcode.pid;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * PID Controller
 */
public class PIDWrapper {
    public PIDCoefficients coefficients;

    private MotionProfile profile;
    private double integralSum = 0;
    private double lastError = 0;

    private int targetPosition = 0;

    private DcMotorEx motor;

    private boolean busy = false;

    private ElapsedTime time = new ElapsedTime();

    private ElapsedTime timer = new ElapsedTime();

    // specify coefficients/gains
    PIDCoefficients coeffs = new PIDCoefficients(0.08, 0, 0.00007);
    // create the controller
    PIDFController controller = new PIDFController(coeffs);

    public PIDWrapper() {
        this(null);
    }

    public PIDWrapper(DcMotorEx motor) {
        this(motor, new PIDCoefficients());
    }

    public PIDWrapper(DcMotorEx motor, PIDCoefficients coefficients) {
        this.coefficients = coefficients;
        this.motor = motor;
        MotionProfileGenerator.generateSimpleMotionProfile(
                new MotionState(0, 0, 0),
                new MotionState(0, 0, 0),
                25,
                40,
                100
        );
    }

    public void update() {
        if (busy) {
            double power = getPID(targetPosition, motor.getCurrentPosition());
            motor.setPower(power);

            if (power == 0.0f && motor.getCurrentPosition() == targetPosition) {
                busy = false;
            }
        }
    }

    /**
     * Get PID values by using f(t)=K_p P(t)+K_i I(t)+K_d D(t) formula
     * @param target Constant value from where you want to go
     * @param state Where the value is currently
     * @return calculated values from f(t)=K_p P(t)+K_i I(t)+K_d D(t)
     */
    public double getPID(double target, double state) {
        double error = target - state;
        integralSum += error * time.seconds();
        double derivative = (error - lastError) / time.seconds();
        lastError = error;

        time.reset();

        double output = (error * coefficients.kP) + (integralSum * coefficients.kI) + (derivative * coefficients.kD);
        return output;
    }

    public double getMotion(double feedback) {
        MotionState state = profile.get(timer.milliseconds());

        controller.setTargetPosition(state.getX());
        controller.setTargetVelocity(state.getV());
        controller.setTargetAcceleration(state.getA());

        return controller.update(feedback);
    }

    public void setPosition(double target, double start) {
        profile = MotionProfileGenerator.generateSimpleMotionProfile(
                new MotionState(start, 0, 0),
                new MotionState(target, 0, 0),
                25,
                40,
                100
        );
    }

    public void setTargetPosition(int position) {
        setTargetPosition(position, false);
    }

    public void setTargetPosition(int position, boolean value) {
        if (targetPosition == position && value) {
            return;
        }

        this.targetPosition = position;

        busy = true;
    }

    protected void setMotor(DcMotorEx motor) {
        this.motor = motor;
    }

    public boolean isBusy() {
        return busy;
    }

    public PIDCoefficients getCoefficients() {
        return coefficients;
    }

    protected void setCoefficients(PIDCoefficients coefficients) {
        this.coefficients = coefficients;
    }
}
