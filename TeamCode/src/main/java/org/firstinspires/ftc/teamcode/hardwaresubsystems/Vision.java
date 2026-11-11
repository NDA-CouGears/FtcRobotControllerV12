package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import android.icu.text.Transliterator;
import android.util.Size;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.RobotParent;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.ArrayList;

public class Vision extends HardwareSubsystem{
    private VisionPortal ballPortal;
    private AprilTagProcessor tagProcessor;
    private VisionPortal tagPortal;
    private Servo elevator;

    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);

        // to-do: add ball cam to the config and uncomment this + the line in end()
        /*
        ballPortal = new VisionPortal.Builder()
                .setCamera(robot.hardwareMap.get(WebcamName.class, "ball cam"))
                .build();
         */

        Position cameraPosition = new Position(DistanceUnit.INCH,
                0, 0, 13, 0);
        YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
                0, 45, 0, 0);

        tagProcessor = new AprilTagProcessor.Builder()
                .setCameraPose(cameraPosition,cameraOrientation)
                .build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(robot.hardwareMap.get(WebcamName.class, "tag cam"));
        builder.setCameraResolution(new Size(320, 240));
        builder.enableLiveView(false);
        builder.addProcessor(tagProcessor);
        tagPortal = builder.build();

        elevator = robot.hardwareMap.get(Servo.class, "elevator");
        elevator.setPosition(.75); // we found that this angle works to see the tags
    }

    @Override
    public void loop() {
        ArrayList<AprilTagDetection> detections = tagProcessor.getDetections();
        robot.telemetry.addLine("detected tags:");
        for (AprilTagDetection detection : detections) {
            if (detection instanceof AprilTagClusterDetection) {
                AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                if (clusterDet.metadata == null)
                    continue;
                robot.telemetry.addLine(String.format("\n==== Tag Cluster (%s)", clusterDet.metadata.name));
                robot.telemetry.addLine(String.format("Percent tags found: %d", clusterDet.percentClusterFound));
                robot.telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
            }
        }
    }

    @Override
    public void end() {
        // ballPortal.close();
        tagPortal.close();
    }
}