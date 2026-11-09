package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.hardwaresubsystems.DriveTrain;

import java.util.Collections;
import java.util.Set;

public class DriveTo implements Command {
    private final RobotParent robot;
    private Path path;

    private DriveTo(RobotParent robot, Path path) {
        this.robot = robot;
        this.path = path;
    }

    /**
     * @param heading1 should be in degrees
     */
    public static DriveTo line(RobotParent robot, double x1, double y1, double heading1, double x2, double y2, double heading2) {
        Pose start = DriveTrain.createPose(x1, y1, heading1);
        Pose end = DriveTrain.createPose(x2, y2, heading2);
        Path path  = Paths.line(start, end).linear(start, end);
        return new DriveTo(robot, path);
    }

    public static DriveTo curve(RobotParent robot, double x1, double y1, double heading1,
                                double xC, double yC, double headingC,
                                double x2, double y2, double heading2) {
        Pose start = DriveTrain.createPose(x1, y1, heading1);
        Pose control = DriveTrain.createPose(xC, yC, headingC);
        Pose end = DriveTrain.createPose(x2, y2, heading2);
        Path path  = Paths.curve(start, control, end).linear(start, end);
        return new DriveTo(robot, path);
    }

    @Override
    public Set<Object> requirements() {
        return Collections.singleton(robot.drive);
    }

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public InterruptedBehavior interruptedBehavior() {
        return InterruptedBehavior.END;
    }

    @Override
    public ConflictBehavior conflictBehavior() {
        return ConflictBehavior.OVERRIDE;
    }

    @Override
    public BlockedBehavior blockedBehavior() {
        return BlockedBehavior.QUEUE;
    }

    @Override
    public void start() {
        robot.drive.driveToPath(path);
    }

    @Override
    public boolean done() {
        // it's done if drive isn't busy (sri's second comment of the season)
        return !robot.drive.isBusy();
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(EndCondition endCondition) {
    }
}
