package src.model;

public class ControllerCommand {

    private final String rawCommand;

    public ControllerCommand(String rawCommand) {
        this.rawCommand = rawCommand != null ? rawCommand.trim() : "";
    }

    public String getCommandAction() {
        return rawCommand;
    }

    public boolean isValid() {

        if (rawCommand.isEmpty())
            return false;

        return rawCommand.startsWith("BTN_")
                || rawCommand.startsWith("MOUSE_");
    }
}