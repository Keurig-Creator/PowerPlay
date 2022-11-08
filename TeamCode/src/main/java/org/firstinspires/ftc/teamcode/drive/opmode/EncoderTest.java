package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.drive.RobotHardware;

@TeleOp(name = "Testing")
public class EncoderTest extends RobotHardware {

    @Override
    public void runOpMode() throws InterruptedException {
        init(hardwareMap);

        horizontalSlide.setPosition(500, horizontalSlide.getMotor().getCurrentPosition());

        waitForStart();

        while (opModeIsActive()) {
            double power = horizontalSlide.getMotion(horizontalSlide.getMotor().getCurrentPosition());

            horizontalSlide.getMotor().setPower(power);
            telemetry.addData("power", power);
            telemetry.addData("current position", horizontalSlide.getMotor().getCurrentPosition());
            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }
}
