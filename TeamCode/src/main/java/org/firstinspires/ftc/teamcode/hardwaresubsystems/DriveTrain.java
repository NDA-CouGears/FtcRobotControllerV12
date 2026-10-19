package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.RobotParent;

public class DriveTrain extends HardwareSubsystem {
    private DcMotorEx leftFront;
    private DcMotorEx rightFront;
    private DcMotorEx leftBack;
    private DcMotorEx rightBack;

    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);
        leftFront = robot.hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = robot.hardwareMap.get(DcMotorEx.class, "rightFront");
        leftBack = robot.hardwareMap.get(DcMotorEx.class, "leftBack");
        rightBack = robot.hardwareMap.get(DcMotorEx.class, "rightBack");
    }

    @Override
    public void loop() {

    }

    @Override
    public void end() {
        
    }
}
