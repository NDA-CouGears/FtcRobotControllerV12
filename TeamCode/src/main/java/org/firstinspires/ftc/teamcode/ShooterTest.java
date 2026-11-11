package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

//import org.firstinspires.ftc.teamcode.legacy.RobotParent;
@TeleOp(name = "Shooter Test")
public class ShooterTest extends LinearOpMode {
    DcMotorEx shoot;
    DcMotorEx intake;
    Servo kicker;
    Servo elevator;

    float speed;

    @Override
    public void runOpMode() {
        waitForStart();
        shoot = hardwareMap.get(DcMotorEx.class, "shoot");
        shoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shoot.setDirection(DcMotorSimple.Direction.FORWARD);

        intake = hardwareMap.get(DcMotorEx.class, "intake spinny");
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        kicker = hardwareMap.get(Servo.class, "kicker");
        double kickerPos = 0;

        elevator = hardwareMap.get(Servo.class, "elevator");
        double elePos = 0;

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

            if (gamepad1.dpadDownWasPressed()) {
                kickerPos -= 0.01;
            }
            else if (gamepad1.dpadUpWasPressed()) {
                kickerPos += 0.01;
            }
            else if (gamepad1.leftTriggerWasPressed()) {
                kickerPos = 0.5;
            }
            else if (gamepad1.leftTriggerWasReleased()) {
                kickerPos = 0;
            }
            kicker.setPosition(kickerPos);
            telemetry.addLine("Kicker Position:" + kickerPos);

            /*
            if (gamepad1.dpadDownWasPressed()) {
                elePos -= 0.05;
            }
            else if (gamepad1.dpadUpWasPressed()) {
                elePos += 0.05;
            }
            elevator.setPosition(elePos);
            telemetry.addLine("Elevator Pos: "+elePos);
             */

            telemetry.update();
        }
    }
}
