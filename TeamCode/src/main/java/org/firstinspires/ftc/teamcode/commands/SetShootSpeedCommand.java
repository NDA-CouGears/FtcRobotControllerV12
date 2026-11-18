package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.qualcomm.robotcore.hardware.configuration.ExpansionHubMotorControllerPositionParams;

import org.firstinspires.ftc.teamcode.RobotParent;

import java.util.Collections;
import java.util.Set;

public class SetShootSpeedCommand implements Command {

    public RobotParent robot;
    public double multiplier;
    public SetShootSpeedCommand(RobotParent robot, double multiplier) {
        this.robot = robot;
        this.multiplier = multiplier;
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
        robot.shoot.setShooter(multiplier);
    }

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(EndCondition endCondition) {

    }
}
