package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import org.firstinspires.ftc.teamcode.RobotParent;

public abstract class HardwareSubsystem {
    protected RobotParent robot;
    private String owner;

    public void init(RobotParent robotParent) {
        robot = robotParent;
    }

    public abstract void loop();

    public abstract void end();
}
