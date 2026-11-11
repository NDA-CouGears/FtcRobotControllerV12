package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.DriveTo;
import org.firstinspires.ftc.teamcode.PedroConstants;

import org.firstinspires.ftc.teamcode.RobotParent;

public class DriveTrain extends HardwareSubsystem {
    private Follower follower;
    private static PoseFactory factory;

    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);

        follower = PedroConstants.create(robot.hardwareMap);
        factory = PoseFactory.degrees();
    }

    public DriveTo line(double x1, double y1, double heading1, double x2, double y2, double heading2) {
        return DriveTo.line(robot,x1,y1,heading1,x2,y2,heading2);
    }

    public DriveTo curve(double x1, double y1, double heading1,
                                double xC, double yC, double headingC,
                                double x2, double y2, double heading2) {
        return DriveTo.curve(robot,x1,y1,heading1,xC,yC,headingC,x2,y2,heading2);
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

    public void mirror() {
        double axis = 70.75;
        factory = factory.mirrorY(axis).mirrorY(axis);
    }

    /**
     heading should be in degrees
     */
    public static Pose createPose(double x, double y, double heading) {
        return factory.of(x,y,heading);
    }

    public void manual(double forward, double lateral, double turn) {
        follower.manual(forward, lateral, turn);
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
