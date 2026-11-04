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

public class DriveToHive implements Command {
    private RobotParent robot;

    public DriveToHive(RobotParent robot) {
        this.robot = robot;
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
        robot.drive.setPos(new Pose(0,0,0));
        Path path = Paths.line(robot.drive.getCurrentPos(), robot.drive.createPose(24,24,90));
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
