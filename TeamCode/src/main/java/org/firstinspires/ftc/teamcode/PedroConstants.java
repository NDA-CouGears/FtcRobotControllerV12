package org.firstinspires.ftc.teamcode;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class PedroConstants {

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("lf_drive");
        c.frontRightName.set("rf_drive");
        c.backLeftName.set("lb_drive");
        c.backRightName.set("rb_drive");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("odo");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(3.2503260214497725);
        c.yPodOffset.set(7.961637842373586);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.2088323420044915);
                Controller secondaryTranslationalForward = Controller.proportional(0.07715797026771101);
                Controller primaryTranslationalLateral = Controller.proportional(0.32799901719133717);
                Controller secondaryTranslationalLateral = Controller.proportional(0.1211868725570454);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.016807168583309884));
                c.brake.set(Controller.proportionalFeedforward(0.014286093295813401));

                c.headingFeedback.set(Controller.proportional(2.4414861631692));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.0423088886750469, 0.008308081564036255));

                c.linearBrakeCoefficients.set(Matrix.diag(0.04734334237469142, 0.03341567936638926));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0029563468912756686, 0.0030830633028349746));

                c.maxAchievableForwardVelocity.set(61.12407864217435);
                c.maxAchievableStrafeVelocity.set(50.00235767925593);
                c.naturalForwardDeceleration.set(35.99661505801246);
                c.naturalStrafeDeceleration.set(57.26856862534808);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

}