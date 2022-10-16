package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.drive.RobotHardware;
import org.firstinspires.ftc.teamcode.linearslide.slide.LinearSlide;
import org.firstinspires.ftc.teamcode.linearslide.slide.data.HorizontalSlide;


@TeleOp(name = "TestingTeleop")
@Config
public class Teleop extends RobotHardware {

    public enum Mode {
        MANUEL,
        AUTO
    }

    private FtcDashboard dashboard = FtcDashboard.getInstance();

    public static double power = 1;

    private ElapsedTime runtime = new ElapsedTime();
    Mode mode = Mode.AUTO;


    @Override
    public void runOpMode() throws InterruptedException {

        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        init(hardwareMap);

        runtime.reset();

        while (runtime.seconds() < 0.1) {

        }

        // Print status of initialization
        telemetry.addData("Vertical Slide", horizontalSlide.slideMotorState.name());
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for start
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            telemetry.addData("Motor Power", horizontalSlide.getMotor().getPower());

            if (gamepad1.a) {
                horizontalSlide.mode = HorizontalSlide.Mode.AUTO;
            } else if (gamepad1.b) {
                horizontalSlide.mode = HorizontalSlide.Mode.MANUEL;
            }

            if (horizontalSlide.mode == LinearSlide.Mode.MANUEL) {
                if (gamepad1.left_bumper) {
                    horizontalSlide.extend(power);
                } else if (gamepad1.right_bumper) {
                    horizontalSlide.retract(power);
                } else {
                    horizontalSlide.getMotor().setPower(0);
                }

                horizontalSlide.retractCheck();
            }
            else {
                int targetPosition = horizontalSlide.targetPosition;
                if (targetPosition > 1200) {
                    targetPosition = 1200;
                }
                double power = horizontalSlide.getPID(targetPosition, horizontalSlide.getMotor().getCurrentPosition());
                horizontalSlide.getMotor().setPower(power);
                telemetry.addData("power", "power");
            }


//            if (!verticalSlide.isBusy()) {
//                if (gamepad1.a) {
//                    mode = Mode.MANUEL;
//                    verticalSlide.setTargetPosition(1000);
//                } else if (gamepad1.b) {
//                    mode = Mode.MANUEL;
//                    verticalSlide.setTargetPosition(0);
//                } else if (gamepad1.y) {
//                    mode = Mode.AUTO;
//                }
//            }
//
//            if (mode == Mode.AUTO) {
//                verticalSlide.setTargetPosition(VerticalSlide.targetPosition, false);
//            }
//
//            verticalSlide.update();

            telemetry.addData("targetPosition", horizontalSlide.targetPosition);
            telemetry.addData("motorPosition", horizontalSlide.getMotor().getCurrentPosition());

            telemetry.update();
        }
    }

    public void updateDrive() {
        double y = -gamepad1.left_stick_y; // value must be reversed
        double x = gamepad1.left_stick_x * 1.1;
        double rotate = gamepad1.right_stick_x;

        // Read inverse IMU heading, as the IMU heading is CW positive
        double botHeading = -imu.getAngularOrientation().firstAngle;

        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rotate), 1);
        double frontLeftPower = (rotY + rotX + rotate) / denominator;
        double backLeftPower = (rotY - rotX + rotate) / denominator;
        double frontRightPower = (rotY - rotX - rotate) / denominator;
        double backRightPower = (rotY + rotX - rotate) / denominator;

        // Update Motor Values
        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
    }


}
