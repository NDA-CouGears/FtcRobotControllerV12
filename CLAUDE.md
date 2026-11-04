# CLAUDE.md

FTC team **NDA CouGears** robot code for the 2026–2027 season (BIOBUZZ), built on the official FTC SDK v12.0. Most contributors are students, so keep changes small, readable, and well commented.

## Where to work

- **All team code lives in `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`.** Edit only here unless asked otherwise.
- `FtcRobotController/` is the upstream FIRST SDK module. Don't modify it; it gets replaced on SDK upgrades. Its `external/samples/` folder is useful reference for SDK APIs (AprilTag, VisionPortal, gamepad, IMU).
- Don't edit `build.common.gradle`. Add dependencies in `build.dependencies.gradle`, and put TeamCode-specific build settings in `TeamCode/build.gradle`.

## Build

```bash
./gradlew :TeamCode:assembleDebug        # compile check (use gradlew.bat in PowerShell)
```

There are no unit tests. Deploying needs Android Studio and a Control Hub, so a successful compile is the only check that can run here. Say so when you hand off work that hasn't been run on the robot.

Toolchain constraints:
- Java source/target is **1.8**, and the Control Hub runs **Android 7 (API 24)**. Don't use Java 9+ library APIs such as `Set.of`, `List.of`, `Map.of`, or `Optional.isEmpty`. They compile against compileSdk 30 but throw `NoSuchMethodError` on the robot. Use `Collections.singleton(...)`, `Arrays.asList(...)`, and so on.
- Don't use the `var` keyword, records, switch expressions, or text blocks.

## Architecture (short version; see ARCHITECTURE.md)

- `RobotParent extends OpMode` owns one instance of each subsystem and forwards `init()` and `loop()` to them. Every competition OpMode should extend `RobotParent` and **call `super.init()`, `super.loop()`, and `super.stop()`**. Cleanup belongs in the SDK's `stop()` lifecycle method. A method named `end()` on an OpMode is never called by the SDK.
- `hardwaresubsystems/HardwareSubsystem` is the base class: `init(RobotParent)`, `loop()`, and `end()`. Get hardware through `robot.hardwareMap`, **never** through the static `BlocksOpModeCompanion.hardwareMap`.
- Path following uses **Pedro Pathing v2** (`com.pedropathing:*`). Tuned constants are in `PedroConstants.java`. Treat those numbers as tuning data: don't change them unless asked.
- Commands use **Ivy** (`com.pedropathing.ivy`). Commands implement `Command` and declare `requirements()` as the subsystem objects they use. `RobotParent` owns the scheduler: it calls `Scheduler.reset()` at the end of `init()` and `Scheduler.execute()` at the end of `loop()`. Don't call these from subsystems.
- Units: inches and degrees, through `DriveTrain.createPose(x, y, headingDeg)`. Use this instead of building `Pose` objects by hand, because the factory handles the alliance mirror.

## Hardware configuration names

These strings must match the robot configuration on the Driver Station:

| Name | Device | Used in |
|---|---|---|
| `lf_drive`, `rf_drive`, `lb_drive`, `rb_drive` | drive motors | PedroConstants |
| `odo` | goBILDA Pinpoint | PedroConstants |
| `shoot` | shooter motor (DcMotorEx) | Shoot, ShooterTest |
| `intake spinny` | intake motor | Intake, ShooterTest |
| `tag cam` | AprilTag webcam | Vision |
| `ball cam` | second webcam (planned, not in config yet) | Vision (commented out) |

If you add or rename a device, update this table.

## Conventions

- Register OpModes with `@TeleOp(name = "...")` or `@Autonomous(name = "...")`. An OpMode without an annotation doesn't appear on the Driver Station. `Autonomous.java` and `TeleOp.java` use fully qualified annotation names because their class names clash with the annotations.
- Motor speeds are set with `setVelocity` in ticks per second: `RPM * ticksPerRev / 60`. goBILDA motors have 28 ticks per motor revolution.
- Robot settings that persist between matches (for example, alliance color) go in `Config`, saved as `robot_config.json` through `AppUtil.getSettingsFile`.
- Standalone test OpModes such as `ShooterTest` can extend `LinearOpMode` directly. Competition code should go through `RobotParent`.
