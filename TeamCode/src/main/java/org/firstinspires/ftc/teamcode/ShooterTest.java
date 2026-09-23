package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

//import org.firstinspires.ftc.teamcode.legacy.RobotParent;
@TeleOp(name = "Shooter Test")
public class ShooterTest extends LinearOpMode {
    DcMotor shoot = hardwareMap.get(DcMotor.class, "shoot");
    @Override
    public void runOpMode() {
        while (opModeIsActive()){
            shoot.setPower(gamepad1.right_stick_y);
        }
    }
}
