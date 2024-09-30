package orionkropt;

public class Commands {
    public enum Command {START, NOCOMMAND}

    private static Command command = Command.NOCOMMAND;

    public static void parseCommand(String msg) {
        if (msg == null) {
            System.out.println("Message is null");
            return;
        }
        if (msg.startsWith("/")) {
            if (msg.equals("/start")) {
                command = Command.START;
            } else {
                command = Command.NOCOMMAND;
            }
        }
    }

    public static Command getCommand() {
        return command;
    }

    public static void setCommand(Command newCommand) {
        command = newCommand;
    }
}
