package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.drive.RobotHardware;

@TeleOp()
public class DisplayEncoder extends RobotHardware {
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {
        init(hardwareMap);


        runtime.reset();


        while (runtime.seconds() < 0.3) {

        }

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("vertical", verticalSlide.getMotor().getCurrentPosition());
            telemetry.addData("horizontal", horizontalSlide.getMotor().getCurrentPosition());
            telemetry.update();
        }
    }
}
