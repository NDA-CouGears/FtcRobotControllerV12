package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
import org.firstinspires.ftc.teamcode.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.RobotParent;

import java.util.Locale;

public class Odometry extends HardwareSubsystem {
    private GoBildaPinpointDriver odo;

    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);
        odo = robot.hardwareMap.get(GoBildaPinpointDriver.class, "odo");

        odo.setOffsets(-7.5, 1.25, DistanceUnit.INCH);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);

        // recalibrate odo
        odo.resetPosAndIMU();
    }

    @Override
    public void loop() {
        odo.update();
        Pose2D pos = odo.getPosition();

        // odo telemetry (useless comment by sri)
        String data = String.format(Locale.US, "{X: %.3f, Y: %.3f, H: %.3f}", pos.getX(DistanceUnit.INCH), pos.getY(DistanceUnit.INCH), pos.getHeading(AngleUnit.DEGREES));
        robot.telemetry.addData("Position", data);
        String velocity = String.format(Locale.US,"{XVel: %.3f, YVel: %.3f, HVel: %.3f}", odo.getVelX(DistanceUnit.INCH), odo.getVelY(DistanceUnit.INCH), odo.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES));
        robot.telemetry.addData("Velocity", velocity);
        robot.telemetry.addData("Status", odo.getDeviceStatus());
        robot.telemetry.addData("Pinpoint Frequency", odo.getFrequency()); //prints/gets the current refresh rate of the Pinpoint
        robot.telemetry.update();
    }

    public Pose2D getOdoReading() {
        return odo.getPosition();
    }

    @Override
    public void end() {
        odo.resetPosAndIMU();
    }
}
