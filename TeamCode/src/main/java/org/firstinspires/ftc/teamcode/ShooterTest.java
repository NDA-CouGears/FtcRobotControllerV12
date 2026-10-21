package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

//import org.firstinspires.ftc.teamcode.legacy.RobotParent;
@TeleOp(name = "Shooter Test")
public class ShooterTest extends LinearOpMode {
    DcMotorEx shoot;
    DcMotorEx intake;

    @Override
    public void runOpMode() {
        waitForStart();
        shoot = hardwareMap.get(DcMotorEx.class, "shoot");
        shoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shoot.setDirection(DcMotorSimple.Direction.REVERSE);

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        while (opModeIsActive()){
            // target RPM * ticks per revolution / 60 (ticks per minute / 60 = ticks per second)
            int shootSpeed = 6000*28/60;

            // If the right joystick is pressed more than a little use it for shoot speed
            if (Math.abs(gamepad1.right_stick_y) > 0.1)
                shoot.setVelocity(gamepad1.right_stick_y*shootSpeed);
            else if (gamepad1.a)
                shoot.setVelocity(shootSpeed*.5);
            else if (gamepad1.b)
                shoot.setVelocity(shootSpeed);
            else if (gamepad1.x)
                shoot.setVelocity(0);

            telemetry.addLine(String.valueOf(shoot.getPower()));

            // gear ratio / 60 (ticks per minute / 60 = ticks per second)
            int intakeSpeed = 6000*28/60;

            // If the left joystick is pressed more than a little use it for intake speed
            if (Math.abs(gamepad1.left_stick_y) > 0.1)
                intake.setVelocity(gamepad1.left_stick_y*intakeSpeed);
            else
                intake.setVelocity(0);

            telemetry.addLine(String.valueOf(intake.getPower()));

            telemetry.update();
        }
    }
}
