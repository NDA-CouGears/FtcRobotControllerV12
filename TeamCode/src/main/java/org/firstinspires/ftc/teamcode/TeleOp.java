package org.firstinspires.ftc.teamcode;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "teleOp")
public class TeleOp extends RobotParent{

    // Driver stick control, run as a command so assists can take over the drive train.
    private ManualDrive manualDrive;

    @Override
    public void init() {
        super.init(); // resets the Scheduler, so schedule commands after this

        manualDrive = new ManualDrive(this, gamepad1);
        Scheduler.schedule(manualDrive);
    }

    @Override
    public void loop() {
        // Button bindings for driver assists go here, for example:
        // if (gamepad1.aWasPressed()) {
        //     runAssist(drive.line(0, 0, 0, 24, 0, 0));
        // }

        super.loop(); // runs the Scheduler (ManualDrive and any assists)
    }

    @Override
    public void stop() {
        super.stop();
    }

    /**
     * Schedule a driver-assist command (or a group of commands) that the driver
     * can cancel at any time by moving the drive sticks.
     *
     * If the assist requires robot.drive, it pauses ManualDrive. When the assist
     * finishes or the driver cancels it, ManualDrive resumes automatically.
     */
    public void runAssist(Command assist) {
        Scheduler.schedule(assist.until(this::driverWantsControl));
    }

    /**
     * True when the driver wants to take back control from an assist:
     * either they are moving the drive sticks or they pressed a cancel input.
     */
    private boolean driverWantsControl() {
        return manualDrive.driverIsDriving() || cancelPressed();
    }

    /** Buttons the driver can press to cancel an assist. Add more inputs here. */
    private boolean cancelPressed() {
        return gamepad1.x;
    }
}
