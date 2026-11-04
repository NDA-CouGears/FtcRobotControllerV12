# Architecture

Snapshot as of 2026-11-03 (commit `9cd64b5`). The codebase is early in the season: the structure is in place, but most subsystems are still stubs.

## Repository layout

```
FtcRobotControllerV12/
├── FtcRobotController/      Upstream FIRST SDK app module (v12.0). Don't edit.
│   └── .../external/samples/   66 sample OpModes, useful as API reference
├── TeamCode/                Team code module (an Android app module that depends on FtcRobotController)
│   ├── build.gradle
│   └── src/main/java/org/firstinspires/ftc/teamcode/
│       ├── RobotParent.java          Base OpMode: owns and forwards to all subsystems
│       ├── Autonomous.java           "Auto": schedules DriveToHive on start
│       ├── TeleOp.java               Empty, not registered yet
│       ├── DriveToHive.java          Ivy Command: drive a line to (24, 24, 90°)
│       ├── ShooterTest.java          Standalone LinearOpMode for shooter/intake prototyping
│       ├── PedroConstants.java       Pedro drivetrain, localizer, and controller tuning
│       ├── GoBildaPinpointDriver.java  Old local copy of the Pinpoint driver (see issues)
│       └── hardwaresubsystems/
│           ├── HardwareSubsystem.java  Abstract base: init / loop / end + lock/unlock
│           ├── DriveTrain.java         Wraps the Pedro Follower and PoseFactory; runs Ivy Scheduler
│           ├── Intake.java             Intake motor
│           ├── Shoot.java              Shooter motor
│           ├── Vision.java             AprilTag camera (stub)
│           └── Config.java             Persistent settings (alliance) + gamepad menu
├── build.common.gradle      Shared Android config (compileSdk 30, minSdk 24, Java 8)
├── build.dependencies.gradle  FTC SDK 12.0.0 + Pedro Pathing + Ivy
└── settings.gradle          Modules: :FtcRobotController, :TeamCode
```

## Dependencies

| Library | Version | Role |
|---|---|---|
| `org.firstinspires.ftc:*` | 12.0.0 | FTC SDK (RobotCore, Hardware, Vision, …) |
| `com.pedropathing:revhub` | 3.0.2 | Pedro hardware bindings: `Mecanum`, `PinpointLocalizer` |
| `com.pedropathing:tuning` | 1.0.0 | Pedro tuning OpModes |
| `com.pedropathing.ivy:pedro` | 1.1.1 | Ivy command scheduler (`Command`, `Scheduler`) |

These are resolved from Maven Central and `repo.dairy.foundation/releases`.

## Runtime model

```
Driver Station selects OpMode
        │
        ▼
Autonomous / TeleOp  ──extends──►  RobotParent (OpMode)
                                     │  init(): each subsystem.init(this)
                                     │  loop(): each subsystem.loop()
                                     ▼
        ┌────────────┬──────────┬──────────┬──────────┬──────────┐
        DriveTrain    Config     Intake     Shoot      Vision
        │
        ├─ Follower (PedroConstants.create)
        │     ├─ PinpointLocalizer  ("odo")
        │     ├─ Mecanum            (lf/rf/lb/rb_drive)
        │     └─ Foresight          (tuned controllers)
        ├─ PoseFactory.degrees()   inches + degrees, alliance mirroring
        └─ loop(): follower.update(); Scheduler.execute()
                                   │
                                   ▼
                         Ivy Commands (e.g. DriveToHive)
                         requirements() → subsystem objects
```

**Subsystem pattern.** Each piece of hardware is a `HardwareSubsystem` subclass. `RobotParent` creates one of each as a public field, so OpModes and Commands reach hardware as `robot.drive`, `robot.shoot`, and so on. Subsystems receive the `RobotParent` in `init()` and use `robot.hardwareMap` to look up devices.

**Command pattern.** Autonomous behavior is written as Ivy `Command`s. Each command declares the subsystems it needs (`requirements()`), its priority, and how it behaves on conflict, interruption, or blocking. The scheduler is reset in `DriveTrain.init()` and ticked from `DriveTrain.loop()`, after the follower updates. As a result, commands only run while `RobotParent.loop()` is called.

**Two concurrency mechanisms.** `HardwareSubsystem` has its own string-based `lock`/`unlock` ownership check, and Ivy has `requirements()`. Nothing calls `lock`/`unlock` yet. The team should pick one, probably Ivy's.

**Persistent config.** `Config` stores `blueAlliance` in `robot_config.json` on the Control Hub, so a pre-match menu, driven by `displayMenu` with dpad left/right, can set the alliance once for both Auto and TeleOp. `DriveTrain.mirror(axis)` is the hook for flipping paths on the red alliance.

## Known issues and gaps

Ordered roughly by how soon they'll cause trouble on the robot:

1. **`DriveToHive` uses `Set.of(...)`** (Java 9). This compiles but throws `NoSuchMethodError` on the Control Hub (Android 7, API 24), so Auto crashes when the command is scheduled. Use `Collections.singleton(robot.drive)` instead.
2. **`Intake` uses the static `BlocksOpModeCompanion.hardwareMap`** instead of `robot.hardwareMap`. That field is null outside Blocks, so `init()` will NPE.
3. **`Vision` calls `hardwareMap.get(VisionPortal.class, "tag cam")`.** `VisionPortal` isn't a hardware device. Get a `WebcamName` and build the portal with `VisionPortal.Builder` plus an `AprilTagProcessor`; see `ConceptAprilTag` in the samples.
4. **`TeleOp` has no `@TeleOp` annotation**, and its `init()`/`loop()` overrides don't call `super`. It won't appear on the Driver Station, and if it did, no subsystem would run.
5. **`RobotParent` never calls `end()` on subsystems.** Override `stop()` to forward it, so the shooter and follower stop cleanly.
6. **`DriveToHive.done()` always returns `false`**, so the command never finishes and holds `drive` forever. It should finish when the follower is no longer busy.
7. **`Config.save()` builds the JSON but never writes it** (`writer.write(json.toString())` is missing), and `load()` is never called.
8. **`Intake.setIntakeSpeed`** clips the input to 0–1 and passes it straight to `setVelocity` (ticks/s), so the motor barely moves. `INTAKE_SPEED` is unused; the intended call is probably `setVelocity(speed * INTAKE_SPEED)`.
9. **Motor name mismatch:** `ShooterTest` uses `"intake"` while `Intake` uses `"intake spinny"`. Only one of them can match the robot configuration.
10. **Local `GoBildaPinpointDriver.java`** duplicates the SDK's built-in `com.qualcomm.hardware.gobilda.GoBildaPinpointDriver`, which `PedroConstants` uses. If the local copy is annotated as an I²C device, it can show up as a second "Pinpoint" type in the configuration UI. It's probably safe to delete.
11. Scheduler ownership lives in `DriveTrain`. If another subsystem ever needs to run without the drive, move `Scheduler.reset()` and `Scheduler.execute()` into `RobotParent`.
