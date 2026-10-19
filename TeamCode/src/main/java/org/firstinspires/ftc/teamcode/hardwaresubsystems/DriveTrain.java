package org.firstinspires.ftc.teamcode.hardwaresubsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.RobotParent;

public class DriveTrain extends HardwareSubsystem {
    private DcMotorEx leftFront;
    private DcMotorEx rightFront;
    private DcMotorEx leftBack;
    private DcMotorEx rightBack;

    @Override
    public void init(RobotParent robotParent) {
        super.init(robotParent);
        leftFront = robot.hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = robot.hardwareMap.get(DcMotorEx.class, "rightFront");
        leftBack = robot.hardwareMap.get(DcMotorEx.class, "leftBack");
        rightBack = robot.hardwareMap.get(DcMotorEx.class, "rightBack");

        leftFront.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftBack.setDirection(DcMotorEx.Direction.FORWARD);
        rightBack.setDirection(DcMotorEx.Direction.REVERSE);
    }

    public void moveRobot(double x, double y, double heading) {
        // make forward = positive y, right = positive x, and clockwise = positive yaw
        //x=-x;
        y = -y;
        heading = -heading;

        // calculate wheel powers.
        double leftFrontPower = y - x - heading;
        double rightFrontPower = y + x + heading;
        double leftBackPower = y + x - heading;
        double rightBackPower = y - x + heading;

        // normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        // send powers to the wheels
        leftFront.setPower(leftFrontPower);
        rightFront.setPower(rightFrontPower);
        leftBack.setPower(leftBackPower);
        rightBack.setPower(rightBackPower);
    }

    public boolean isMoving() {
        return leftBack.getVelocity()!=0
                || leftFront.getVelocity()!=0
                || rightBack.getVelocity()!=0
                || rightFront.getVelocity()!=0;
    }

    @Override
    public void loop() {

    }

    @Override
    public void end() {
        leftFront.setVelocity(0);
        rightFront.setVelocity(0);
        leftBack.setVelocity(0);
        rightBack.setVelocity(0);
    }
}
