package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.hardwaresubsystems.DriveTrain;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "auto")
public class Autonomous extends RobotParent{

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        super.start();
        drive.mirror();
        drive.setPos(DriveTrain.createPose(8.5,8.5,90));
        Scheduler.schedule(
                sequential(
                        DriveTo.line(this,8.5,8.5,90,55,8.5,90),
                        DriveTo.curve(this,55,8.5,90,70.75,70.75,180,55,141.5-8.5,270)

                )
            );
        // command that parks
        // shoot,
    }

    @Override
    public void loop() {
        super.loop();
    }

    @Override
    public void stop() {
        super.stop();
    }
}
