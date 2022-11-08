package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.drive.RobotHardware;


@TeleOp(name = "PreInitialize")
@Config
public class PreInitialize extends RobotHardware {

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

        sleep(1000);

        horizontalSlide.getMotor().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        horizontalSlide.getMotor().setPower(0.3);

        sleep(1000);

        horizontalSlide.getMotor().setPower(0);

        verticalSlide.home();

        sleep(1000);

        runtime.reset();

        while (runtime.seconds() < 2) {
            double power = verticalSlide.getPID(500, verticalSlide.getMotor().getCurrentPosition());
            verticalSlide.getMotor().setPower(power / 2);
        }

        verticalSlide.getMotor().setPower(0);

        sleep(500);

        horizontalSlide.home();


        // Print status of initialization
        telemetry.addData("Vertical Slide", verticalSlide.slideMotorState.name());
        telemetry.addData("Horizontal Slide", horizontalSlide.slideMotorState.name());
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for start
        waitForStart();

        if (isStopRequested()) return;

        double rotatorPosition = 0;

        while (opModeIsActive()) {

//            if (gamepad1.a) {
//                verticalSlide.mode = VerticalSlide.Mode.AUTO;
//            } else if (gamepad1.b) {
//                verticalSlide.mode = VerticalSlide.Mode.MANUEL;
//            }
//
//            if (verticalSlide.mode == LinearSlide.Mode.MANUEL) {
//                if (gamepad1.left_bumper) {
//                    verticalSlide.extend(power);
//                } else if (gamepad1.right_bumper) {
//                    verticalSlide.retract(power);
//                } else {
//                    verticalSlide.getMotor().setPower(0);
//                }
//
//                verticalSlide.retractCheck();
//            }
//            else {
//                int targetPosition = verticalSlide.targetPosition;
//                if (targetPosition > 1200) {
//                    targetPosition = 1200;
//                }
//                double power = verticalSlide.getPID(targetPosition, verticalSlide.getMotor().getCurrentPosition());
//                verticalSlide.getMotor().setPower(power / 2);
//                telemetry.addData("power", "power");
//            }


            telemetry.addData("targetPosition", verticalSlide.targetPosition);
            telemetry.addData("motorPosition", verticalSlide.getMotor().getCurrentPosition());

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
