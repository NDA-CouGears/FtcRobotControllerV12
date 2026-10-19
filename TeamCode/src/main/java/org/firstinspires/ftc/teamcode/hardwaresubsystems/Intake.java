public class Intake extends HardwareSubsystem{
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