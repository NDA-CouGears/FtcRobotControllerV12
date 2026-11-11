package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import android.icu.text.Transliterator;
import android.util.Size;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
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
import java.util.concurrent.TimeUnit;

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

    private void optimizeExposureForAprilTags(VisionPortal portal) {
        // 1. Ensure the vision portal and camera are fully initialized and streaming
        if (portal == null) return;

        // Wait until the camera state transitions to STREAMING
        if (portal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            robot.telemetry.addData("Camera", "Waiting for streaming state...");
            robot.telemetry.update();
            return;
        }

        // 2. Grab the Exposure and Gain control interfaces from the portal
        ExposureControl exposureControl = portal.getCameraControl(ExposureControl.class);
        GainControl gainControl = portal.getCameraControl(GainControl.class);

        // 3. Change Mode to Manual (Required before you can override values)
        if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
            exposureControl.setMode(ExposureControl.Mode.Manual);
            // sleep(50);
        }

        // 4. Set standard low-shutter, high-gain parameters to kill motion blur
        // Target exposure: 4 to 8 milliseconds (very fast)
        // Target gain: High amplification (usually 200 - 250, depending on your webcam)
        long targetExposureMs = 6;
        int targetGain = 250;

        // Clamp the values to what your physical webcam firmware supports
        long minExp = exposureControl.getMinExposure(TimeUnit.MILLISECONDS);
        long maxExp = exposureControl.getMaxExposure(TimeUnit.MILLISECONDS);
        int minGain = gainControl.getMinGain();
        int maxGain = gainControl.getMaxGain();

        long finalExposure = Math.max(minExp, Math.min(maxExp, targetExposureMs));
        int finalGain = Math.max(minGain, Math.min(maxGain, targetGain));

        // 5. Commit the settings to hardware
        exposureControl.setExposure(finalExposure, TimeUnit.MILLISECONDS);
        gainControl.setGain(finalGain);

        robot.telemetry.addData("Camera", "Manual Control Locked!");
        robot.telemetry.addData("Exposure set to", finalExposure + "ms");
        robot.telemetry.addData("Gain set to", finalGain);
        robot.telemetry.update();
    }

}