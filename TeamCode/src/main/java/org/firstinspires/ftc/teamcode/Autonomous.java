package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.commands.SetShootSpeedCommand;
import org.firstinspires.ftc.teamcode.commands.ShootCommand;
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
        drive.setPos(DriveTrain.createPose(56,8.5,90));
        Scheduler.schedule(
                sequential(
                        // shoot UNTESTED !!!!!!!!!!!!!!!
                        new SetShootSpeedCommand(this,.5),
                        new ShootCommand(this,4),
                        // park
                        drive.curve(56,8.5,90,24,37,90,9,100,90)
                ));
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
