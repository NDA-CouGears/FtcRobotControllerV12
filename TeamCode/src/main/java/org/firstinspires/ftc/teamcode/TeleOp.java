package org.firstinspires.ftc.teamcode;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "teleOp")
public class TeleOp extends RobotParent{

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        super.loop();

        double forward = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        drive.manual(forward, lateral, turn);
    }

    @Override
    public void stop() {
        super.stop();
    }
}
