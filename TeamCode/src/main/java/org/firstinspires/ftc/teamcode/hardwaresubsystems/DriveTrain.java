package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.PedroConstants;

import org.firstinspires.ftc.teamcode.RobotParent;

public class DriveTrain extends HardwareSubsystem {
    private Follower follower;
    private PoseFactory factory;

    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);

        follower = PedroConstants.create(robot.hardwareMap);
        factory = PoseFactory.degrees();
    }

    public void setPos(Pose pose) {
        follower.setPose(pose);
        follower.update();
    }

    public boolean isBusy() {
        return follower.isBusy();
    }

    public void driveToPath(Path path) {
        follower.follow(path);
    }

    public Pose getCurrentPos() {
        return follower.pose();
    }

    public void mirror(double axis) {
        factory.mirrorY(axis);
    }

    /**
     heading should be in degrees
     */
    public Pose createPose(double x, double y, double heading) {
        return factory.of(x,y,heading);
    }

    @Override
    public void loop() {
        follower.update();
    }

    @Override
    public void end() {
        follower.stop();
    }
}
