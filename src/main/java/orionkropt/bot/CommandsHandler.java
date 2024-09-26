package orionkropt.bot;

import java.util.HashMap;

public class CommandsHandler {
    public enum Command {START, NOCOMMAND}

    private static Command command = Command.NOCOMMAND;
    private final static HashMap<String, Command> commands = new HashMap<>() {{
        put("/start", Command.START);
    }};

    public static void parseCommand(String msg) {
        if (msg == null) {
            System.out.println("Message is null");
            return;
        }
        if (msg.startsWith("/")) {
            if (!commands.containsKey(msg)) {
                command = Command.NOCOMMAND;
                return;
            }
            command = commands.get(msg);
        }
    }

    public static Command getCommand() {
        return command;
    }

    public static void setCommand(Command newCommand) {
        command = newCommand;
    }
}
