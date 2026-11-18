package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;

import org.firstinspires.ftc.teamcode.RobotParent;

import java.util.Collections;
import java.util.Set;

public class ControlIntake implements Command {

    private RobotParent robot;
    private double speed;

    private ControlIntake(RobotParent robot, double speed) {
        this.robot = robot;
        this.speed = speed;
    }

    public static ControlIntake intakeOn(RobotParent robot, double speed, boolean forward) {
        if (forward) speed = Math.abs(speed);
        else speed = -Math.abs(speed);
        return new ControlIntake(robot,speed);
    }

    public static ControlIntake intakeOff(RobotParent robot) {
        return new ControlIntake(robot,0);
    }

    @Override
    public Set<Object> requirements() {
        return Collections.emptySet();
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public InterruptedBehavior interruptedBehavior() {
        return null;
    }

    @Override
    public ConflictBehavior conflictBehavior() {
        return null;
    }

    @Override
    public BlockedBehavior blockedBehavior() {
        return null;
    }

    @Override
    public void start() {
        robot.intake.setIntakeSpeed(speed);
    }

    @Override
    public boolean done() {
        return true;
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(EndCondition endCondition) {

    }
}
