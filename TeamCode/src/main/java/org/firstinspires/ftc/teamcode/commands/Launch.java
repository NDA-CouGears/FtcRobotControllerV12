package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RobotParent;

import java.util.Collections;
import java.util.Set;

public class Launch implements Command {
    private final RobotParent robot;
    private int remainingShots;
    private ElapsedTime timer;
    private int kickState;
    private static final int KICK = 0;
    private static final int WAIT_UP = 1;
    private static final int WAIT_DOWN = 2;
    public Launch(RobotParent robot, int shots) {
        this.robot = robot;
        this.remainingShots = shots;
        timer = new ElapsedTime();
    }

    @Override
    public Set<Object> requirements() {
        return Collections.singleton(robot.shoot);
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

    }

    @Override
    public boolean done() {
        return !robot.shoot.isBusy();
    }

    @Override
    public void execute() {
        // test on new robot
        switch (kickState) {
            case KICK:
                robot.shoot.kick(true);
                kickState = WAIT_UP;
                timer.reset();
                break;
            case WAIT_UP:
                if (timer.milliseconds() > 500) {
                    robot.shoot.kick(false);
                    timer.reset();
                    kickState = WAIT_DOWN;
                }
                break;
            case WAIT_DOWN:
                if (timer.milliseconds() > 500) {
                    remainingShots--;
                    if (remainingShots > 0)
                        kickState = KICK;
                    else break;
                }
        }
    }

    @Override
    public void end(EndCondition endCondition) {
    }
}
