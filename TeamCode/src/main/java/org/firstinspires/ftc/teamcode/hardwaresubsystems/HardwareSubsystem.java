package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import org.firstinspires.ftc.teamcode.RobotParent;

public abstract class HardwareSubsystem {
    protected RobotParent robot;
    private String owner;

    public void init(RobotParent robotParent) {
        robot = robotParent;
    }

    public abstract void loop();

    public abstract void end();

    public void lock(String lockerID) {
        if (owner != null) throw new RuntimeException(lockerID+" tried to lock; already locked by "+owner);
        owner = lockerID;
    }

    public void unlock(String lockerID) {
        if (owner == null) throw new RuntimeException("not locked yet");

        if (lockerID.equals(owner)) {
            owner = null;
        }
        else {
            throw new RuntimeException(lockerID+" tried to unlock, system owned by "+owner);
        }
    }
}
