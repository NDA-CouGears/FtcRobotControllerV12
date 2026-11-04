package org.firstinspires.ftc.teamcode;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "auto")
public class Autonomous extends RobotParent{

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();
        Scheduler.schedule(new DriveToHive(this));
    }

    @Override
    public void loop() {
        super.loop();
    }

    @Override
    public void end() {
        super.end();
    }
}
