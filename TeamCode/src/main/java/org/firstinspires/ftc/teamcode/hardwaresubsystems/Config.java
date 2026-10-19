public class Config extends HardwareSubsystem{
    private static final File CONFIG_FILE = AppUtil.getInstance().getSettingsFile("robot_config.json");
    public blueAlliance = true;

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            JSONObject json = new JSONObject();
            json.put("blueAlliance", blueAlliance);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void load() {
        if (!CONFIG_FILE.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            char[] buffer = new char[(int) CONFIG_FILE.length()];
            reader.read(buffer);
            JSONObject json = new JSONObject(new String(buffer));

            blueAlliance = json.getBoolean("blueAlliance");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    int curMenu = 0;

    boolean dPadPressed = false;

    public void displayMenu(Telemetry telemetry, Gamepad gamepad) {
        boolean changed = false;

        // Display current menu state
        telemetry.addLine("dpad.y to select, dpad.x to change value");
        telemetry.addLine(String.format("%s alliance %s", curMenu == 0 ? "*" : "-", blueAlliance ? "BLUE" : "RED"));


        // Do not allow another input after the dpad was pressed until it is released
        if (dpadPressed &&
                !gamepad.dpad_down &&
                !gamepad.dpad_up &&
                !gamepad.dpad_left &&
                !gamepad.dpad_right) {
            dpadPressed = false;
        }

        if (dpadPressed) {
            // We registered a press and it is still being held down, await a release
            return;
        }

        // Navigate between menu options
        // commented out b/c we only have one option for now
        /*
        if (gamepad.dpad_down && curMenu < 6) {
            curMenu++;
            dpadPressed = true;
        } else if (gamepad.dpad_up && curMenu > 0) {
            curMenu--;
            dpadPressed = true;
        */

        // Update selected value
        double delta = 0;
        if (gamepad.dpad_left) {
            delta = -1;
        } else if (gamepad.dpad_right) {
            delta = 1;
        }

        // Apply any changes
        if (delta != 0) {
            dpadPressed = true;
            changed = true;
            switch (curMenu) {
                case 0:
                    blueAlliance = !blueAlliance;
                    break;
            }
        }

        if (changed) {
            save();
        }



    }