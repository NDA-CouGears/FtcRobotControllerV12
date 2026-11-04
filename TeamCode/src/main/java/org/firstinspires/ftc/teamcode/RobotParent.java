package org.firstinspires.ftc.teamcode;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.hardwaresubsystems.Config;
import org.firstinspires.ftc.teamcode.hardwaresubsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.hardwaresubsystems.Intake;
import org.firstinspires.ftc.teamcode.hardwaresubsystems.Shoot;
import org.firstinspires.ftc.teamcode.hardwaresubsystems.Vision;

public abstract class RobotParent extends OpMode {
    public DriveTrain drive = new DriveTrain();
    public Config config = new Config();
    public Intake intake = new Intake();
    public Shoot shoot = new Shoot();
    public Vision vision = new Vision();


    @Override
    public void init() {
        drive.init(this);
        config.init(this);
        intake.init(this);
        shoot.init(this);
        vision.init(this);
        Scheduler.reset();
    }

    @Override
    public void loop() {
        drive.loop();
        config.loop();
        intake.loop();
        shoot.loop();
        vision.loop();
        Scheduler.execute();
    }

    @Override
    public void stop() {
        drive.end();
        config.end();
        intake.end();
        shoot.end();
        vision.end();
    }
}
