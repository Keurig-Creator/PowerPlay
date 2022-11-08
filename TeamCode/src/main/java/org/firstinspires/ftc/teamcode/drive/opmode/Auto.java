package org.firstinspires.ftc.teamcode.drive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.drive.RobotHardware;

@TeleOp(name = "Auto")
public class Auto extends RobotHardware {

    private boolean aPressed = false;
    private boolean pickingUp = false;

    private double horizontalPower;
    private double verticalPower;

    private ElapsedTime runtime = new ElapsedTime();

    int state = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap);

        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.a) {
                if (!aPressed) {
                    runtime.reset();
                   pickingUp = true;
                }

                aPressed = true;
            } else {
                aPressed = false;
            }

            if (pickingUp) {

                if (runtime.seconds() > 8) {
                    horizontalSlide.openClaw();
                    state = 4;
                } else if (runtime.seconds() > 7) {
                    verticalSlide.closeClaw();
                    state = 3;
                } else if (runtime.seconds() > 5) {
                    horizontalPower = horizontalSlide.getPID(55, horizontalSlide.getMotor().getCurrentPosition());
                    state = 2;
                } else if (runtime.seconds() > 2) {
                    verticalSlide.rotator.setPosition(0);
                    verticalSlide.openClaw();
                    horizontalSlide.closeClaw();
                    state = 1;
                }





                if (state == 0) {
                    horizontalSlide.openClaw();
                    verticalPower = verticalSlide.getPID(240, verticalSlide.getMotor().getCurrentPosition());
                    horizontalPower = horizontalSlide.getPID(1100, horizontalSlide.getMotor().getCurrentPosition());
                }
            }

            verticalPower = Range.clip(verticalPower, -0.5, 0.5);

            horizontalSlide.getMotor().setPower(horizontalPower / 2);
            verticalSlide.getMotor().setPower(verticalPower);
        }

    }
}
