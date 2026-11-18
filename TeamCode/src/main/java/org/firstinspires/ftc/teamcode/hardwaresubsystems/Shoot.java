package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.RobotParent;
import org.firstinspires.ftc.teamcode.commands.SetShootSpeed;

public class Shoot extends HardwareSubsystem {
    private DcMotorEx shooter;
    private Servo kicker;
    // target RPM * ticks per revolution / 60 (ticks per minute / 60 = ticks per second)
    private final int MAX_SHOOT_SPEED = 6000*28/60;


    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);
        shooter = robot.hardwareMap.get(DcMotorEx.class, "shoot");
        kicker = robotParent.hardwareMap.get(Servo.class, "kicker");
        kicker.setPosition(0);
    }

    public boolean isBusy() {
        return shooter.isBusy();
    }

    public SetShootSpeed setShootSpeed(double multiplier) {
        return SetShootSpeed.setShootSpeed(robot,multiplier);
    }

    public void setShooter(double multiplier) {
        multiplier = Range.clip(multiplier,0,1);
        shooter.setVelocity(MAX_SHOOT_SPEED*multiplier);
    }

    public void kick(boolean up) {
        if (up) kicker.setPosition(.5);
        else kicker.setPosition(0);
    }

    @Override
    public void loop() {

    }

    @Override
    public void end() {
        shooter.setVelocity(0);
    }
}
