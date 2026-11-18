package org.firstinspires.ftc.teamcode.commands;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.RobotParent;

import java.util.Collections;
import java.util.Set;

/**
 * Driver-controlled driving, run as an Ivy command for the whole TeleOp.
 *
 * How it shares the drive train with driver-assist commands (like DriveTo):
 *  - It requires robot.drive, so only one drive command runs at a time.
 *  - It has the LOWEST priority (-1), so any other command that requires
 *    robot.drive will interrupt it.
 *  - When interrupted it SUSPENDS instead of ending. The scheduler parks it and
 *    automatically resumes it as soon as the drive train is free again
 *    (when the assist finishes, or the driver cancels it).
 *
 * driverIsDriving() reports when the driver is pushing the sticks. TeleOp uses
 * it to cancel a driver assist so the driver can take over at any time.
 */
public class ManualDrive implements Command {
    // Stick values smaller than this count as "not touching the stick".
    public static final double OVERRIDE_DEADBAND = 0.2;

    private final RobotParent robot;
    private final Gamepad gamepad;

    public ManualDrive(RobotParent robot, Gamepad gamepad) {
        this.robot = robot;
        this.gamepad = gamepad;
    }

    // True when the driver is moving any drive stick past the deadband.
    public boolean driverIsDriving() {
        return Math.abs(gamepad.left_stick_y) > OVERRIDE_DEADBAND
                || Math.abs(gamepad.left_stick_x) > OVERRIDE_DEADBAND
                || Math.abs(gamepad.right_stick_x) > OVERRIDE_DEADBAND;
    }

    @Override
    public Set<Object> requirements() {
        return Collections.singleton((Object) robot.drive);
    }

    @Override
    public int priority() {
        // Lower than every other command, so anything that needs the drive train wins.
        return -1;
    }

    @Override
    public InterruptedBehavior interruptedBehavior() {
        // Pause (don't end) when an assist takes the drive train; resume after.
        return InterruptedBehavior.SUSPEND;
    }

    @Override
    public ConflictBehavior conflictBehavior() {
        return ConflictBehavior.OVERRIDE;
    }

    @Override
    public BlockedBehavior blockedBehavior() {
        // If something is already driving when we schedule this, wait for it.
        return BlockedBehavior.QUEUE;
    }

    @Override
    public void start() {
    }

    @Override
    public void execute() {
        double forward = -gamepad.left_stick_y;
        double lateral = gamepad.left_stick_x;
        double turn = gamepad.right_stick_x;

        robot.drive.manual(forward, lateral, turn);
    }

    @Override
    public boolean done() {
        // Runs for the whole TeleOp.
        return false;
    }

    @Override
    public void end(EndCondition endCondition) {
    }
}
