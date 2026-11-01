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
    float speed;

    @Override
    public void runOpMode() {
        waitForStart();
        shoot = hardwareMap.get(DcMotorEx.class, "shoot");
        shoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shoot.setDirection(DcMotorSimple.Direction.FORWARD);

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        while (opModeIsActive()){
            // target RPM * ticks per revolution / 60 (ticks per minute / 60 = ticks per second)
            int shootSpeed = 6000*28/60;

            if (gamepad1.a) {
                speed = gamepad1.right_stick_y * shootSpeed;
                shoot.setVelocity(speed);
            }
            // If the right joystick is pressed more than a little use it for shoot speed
            else if (gamepad1.right_stick_y < -0.1 && speed == 0) {
                shoot.setVelocity(gamepad1.right_stick_y * shootSpeed);
                telemetry.addLine(String.valueOf(gamepad1.right_stick_y * shootSpeed));
            }
            else if (gamepad1.x) {
                speed = 0;
                shoot.setVelocity(speed);
            }
            else if (gamepad1.y) {
                speed = -1250;
                shoot.setVelocity(speed);
            }
            else {
                telemetry.addLine(String.valueOf(speed));
            }

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
