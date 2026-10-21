import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.RobotParent;
import org.firstinspires.ftc.teamcode.hardwaresubsystems.HardwareSubsystem;

public class Intake extends HardwareSubsystem {
    private DcMotorEx intakeSpinny = null;

    // target RPM * ticks per revolution / 60 (ticks per minute / 60 = ticks per second)
    private final int INTAKE_SPEED = 1620*28/60;

    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);
        intakeSpinny = hardwareMap.get(DcMotorEx.class, "intake spinny");
    }

    public void setIntakeSpeed(double speed) {
        speed = Range.clip(speed,0,1);
        intakeSpinny.setVelocity(speed);
    }

    public double getIntakeSpeed() {
        return intakeSpinny.getVelocity();
    }

    @Override
    public void loop() {

    }

    @Override
    public void end() {

    }

}