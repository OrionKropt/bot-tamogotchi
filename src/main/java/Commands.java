public class Commands {
    public enum Command {START, NOCOMMAND}

    private static Command command = Command.NOCOMMAND;

    public static void SetCommand(String msg) {
        if (msg.equals("/start")) {
            command = Command.START;
        }
    }

    public static Command GetCommand() {
        return command;
    }
}
