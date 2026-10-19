package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.RobotParent;

public class Shoot extends HardwareSubsystem {
    private DcMotorEx shooter;

    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);
        shooter = robot.hardwareMap.get(DcMotorEx.class, "shoot");
    }

    @Override
    public void loop() {

    }

    @Override
    public void end() {
        shooter.setVelocity(0);
    }
}
