package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import org.firstinspires.ftc.teamcode.RobotParent;

public abstract class HardwareSubsystem {
    protected RobotParent robot;
    private String owner;

    public void init(RobotParent robotParent) {
        robot = robotParent;
    }

    public void init_loop() {}

    public abstract void loop();

    public abstract void end();
}
