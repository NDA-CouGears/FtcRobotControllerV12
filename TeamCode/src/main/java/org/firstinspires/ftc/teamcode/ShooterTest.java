package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

//import org.firstinspires.ftc.teamcode.legacy.RobotParent;
@TeleOp(name = "Shooter Test")
public class ShooterTest extends LinearOpMode {
    DcMotorEx shoot;
    @Override
    public void runOpMode() {
        waitForStart();
        shoot = hardwareMap.get(DcMotorEx.class, "shoot");
        shoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while (opModeIsActive()){
            // target RPM * ticks per revolution / 60 (ticks per minute / 60 = ticks per second)
            int speed = 3000*28/60;
            shoot.setVelocity(speed);
            telemetry.addLine(String.valueOf(shoot.getPower()));
            telemetry.update();
        }
    }
}
