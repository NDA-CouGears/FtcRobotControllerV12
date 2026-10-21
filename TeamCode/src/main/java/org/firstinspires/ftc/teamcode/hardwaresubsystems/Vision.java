package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import org.firstinspires.ftc.teamcode.RobotParent;
import org.firstinspires.ftc.vision.VisionPortal;

public class Vision extends HardwareSubsystem{
    private VisionPortal tagCam;

    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);
        tagCam = robot.hardwareMap.get(VisionPortal.class, "tag cam");
    }

    @Override
    public void loop() {

    }

    @Override
    public void end() {

    }
}