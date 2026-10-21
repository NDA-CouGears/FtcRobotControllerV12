import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.RobotParent;
import org.firstinspires.ftc.teamcode.hardwaresubsystems.HardwareSubsystem;

public class Intake extends HardwareSubsystem {
    private DcMotorEx intakeSpinny = null;

    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);
        intakeSpinny = hardwareMap.get(DcMotorEx.class, "intake spinny");
    }

    @Override
    public void loop() {

    }

    @Override
    public void end() {

    }

}