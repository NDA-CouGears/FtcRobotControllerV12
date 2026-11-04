package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import android.graphics.Color;
import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RobotParent;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;

public class Vision extends HardwareSubsystem{
    private VisionPortal ballPortal;
    private AprilTagProcessor tagProcessor;
    private VisionPortal tagPortal;

    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);

        // to-do: add ball cam to the config
        /*
        ballPortal = new VisionPortal.Builder()
                .setCamera(robot.hardwareMap.get(WebcamName.class, "ball cam"))
                .build();
         */

        tagProcessor = new AprilTagProcessor.Builder()
                .build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(robot.hardwareMap.get(WebcamName.class, "tag cam"));
        builder.setCameraResolution(new Size(320, 240));
        builder.enableLiveView(false);
        builder.addProcessor(tagProcessor);
        tagPortal = builder.build();
    }

    @Override
    public void loop() {

    }

    @Override
    public void end() {

    }
}