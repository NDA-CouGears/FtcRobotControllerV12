# Architecture

Snapshot as of 2026-11-04 (commit `9893242`). The codebase is early in the season: the structure is in place, but most subsystems are still stubs.

## Repository layout

```
FtcRobotControllerV12/
├── FtcRobotController/      Upstream FIRST SDK app module (v12.0). Don't edit.
│   └── .../external/samples/   66 sample OpModes, useful as API reference
├── TeamCode/                Team code module (an Android app module that depends on FtcRobotController)
│   ├── build.gradle
│   └── src/main/java/org/firstinspires/ftc/teamcode/
│       ├── RobotParent.java          Base OpMode: owns and forwards to all subsystems
│       ├── Autonomous.java           "auto": schedules DriveToHive on start
│       ├── TeleOp.java               "teleOp": registered, but runs no subsystems yet
│       ├── DriveToHive.java          Ivy Command: drive a line to (24, 24, 90°)
│       ├── ShooterTest.java          Standalone LinearOpMode for shooter/intake prototyping
│       ├── PedroConstants.java       Pedro drivetrain, localizer, and controller tuning
│       ├── GoBildaPinpointDriver.java  Old local copy of the Pinpoint driver (see issues)
│       └── hardwaresubsystems/
│           ├── HardwareSubsystem.java  Abstract base: init / loop / end + lock/unlock
│           ├── DriveTrain.java         Wraps the Pedro Follower and PoseFactory; runs Ivy Scheduler
│           ├── Intake.java             Intake motor
│           ├── Shoot.java              Shooter motor
│           ├── Vision.java             AprilTag VisionPortal on "tag cam" (ball cam planned)
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
                                     │  init(): …then Scheduler.reset()
                                     │  loop(): each subsystem.loop(), then Scheduler.execute()
                                     ▼
        ┌────────────┬──────────┬──────────┬──────────┬──────────┐
        DriveTrain    Config     Intake     Shoot      Vision
        │
        ├─ Follower (PedroConstants.create)
        │     ├─ PinpointLocalizer  ("odo")
        │     ├─ Mecanum            (lf/rf/lb/rb_drive)
        │     └─ Foresight          (tuned controllers)
        ├─ PoseFactory.degrees()   inches + degrees, alliance mirroring
        └─ loop(): follower.update(); Scheduler.execute()  ← duplicate, see issues
                                   │
                                   ▼
                         Ivy Commands (e.g. DriveToHive)
                         requirements() → subsystem objects
```

**Subsystem pattern.** Each piece of hardware is a `HardwareSubsystem` subclass. `RobotParent` creates one of each as a public field, so OpModes and Commands reach hardware as `robot.drive`, `robot.shoot`, and so on. Subsystems receive the `RobotParent` in `init()` and use `robot.hardwareMap` to look up devices.

**Command pattern.** Autonomous behavior is written as Ivy `Command`s. Each command declares the subsystems it needs (`requirements()`), its priority, and how it behaves on conflict, interruption, or blocking. The scheduler is reset at the end of `RobotParent.init()` and ticked at the end of `RobotParent.loop()`. `DriveTrain` still has its own `Scheduler.reset()` and `Scheduler.execute()` calls left over from before, so commands currently tick twice per loop (see issues).

**Two concurrency mechanisms.** `HardwareSubsystem` has its own string-based `lock`/`unlock` ownership check, and Ivy has `requirements()`. Nothing calls `lock`/`unlock` yet. The team should pick one, probably Ivy's.

**Persistent config.** `Config` stores `blueAlliance` in `robot_config.json` on the Control Hub, so a pre-match menu, driven by `displayMenu` with dpad left/right, can set the alliance once for both Auto and TeleOp. `DriveTrain.mirror(axis)` is the hook for flipping paths on the red alliance.

## Known issues and gaps

Ordered roughly by how soon they'll cause trouble on the robot:

1. **Subsystem `end()` is never called.** `RobotParent.end()` is a new method, not an SDK lifecycle method: the SDK calls `stop()`. Rename it to `@Override public void stop()` in `RobotParent`, `Autonomous`, and `TeleOp`, so the follower and shooter actually stop at the end of a match.
2. **The Ivy scheduler runs twice per loop.** `Scheduler.execute()` and `Scheduler.reset()` are now in `RobotParent`, but the old calls in `DriveTrain.init()` and `DriveTrain.loop()` weren't removed. Every command's `execute()` and `done()` run twice per cycle. Delete the two `DriveTrain` calls.
3. **`TeleOp` doesn't call `super.init()` or `super.loop()`.** It shows up on the Driver Station now, but no hardware is initialized and nothing runs.
4. **`Config.save()` builds the JSON but never writes it** (`writer.write(json.toString())` is missing), and `load()` is never called. Alliance selection won't persist.
5. **`Vision` never closes its portal.** Add `tagPortal.close()` in `Vision.end()`, once fix 1 means `end()` actually runs. A camera left open can fail to start in the next OpMode.
6. **`Intake.setIntakeSpeed` clips the input to 0–1**, so the intake can't run in reverse to clear a jam. Use `Range.clip(speed, -1, 1)` if reverse is wanted.
7. **Local `GoBildaPinpointDriver.java`** duplicates the SDK's built-in `com.qualcomm.hardware.gobilda.GoBildaPinpointDriver`, which `PedroConstants` uses. It's probably safe to delete.
8. **Unused lock system:** nothing calls `HardwareSubsystem.lock()`/`unlock()`. Ivy's `requirements()` already does this job, so the lock methods can go.
9. Minor: `Autonomous.java` has an unused `Command` import.

### Fixed in `d63ec5a`
- `DriveToHive` uses `Collections.singleton` instead of `Set.of`.
- `DriveToHive.done()` finishes when the follower isn't busy.
- `Intake` uses `robot.hardwareMap` and scales by `INTAKE_SPEED`.
- `Vision` builds a real `VisionPortal` with an `AprilTagProcessor`.
- `TeleOp` is annotated.
- `ShooterTest` and `Intake` both use `"intake spinny"`.
