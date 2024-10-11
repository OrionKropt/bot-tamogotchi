package orionkropt.game;

import org.reflections.Reflections;
import orionkropt.game.characters.Character;
import orionkropt.game.characters.CharacterManager;
import orionkropt.game.rooms.Hall;
import orionkropt.game.rooms.Kitchen;
import orionkropt.game.rooms.Room;
import orionkropt.image.Image;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

enum GameState {
    MAIN,
}

public class Game {
    static private final HashMap<Long, GameState> gameStates = new HashMap<>();
    static private final HashMap<Long, Room> userRooms = new HashMap<>();
    static private final HashMap<String, Room> rooms = new HashMap<>();
    static private final HashMap<String, Method> actions = new HashMap<>();
    static private final Render render = new Render();
    static private final String startRoom = "hall";
    private GameState gameState;
    private Image currentImage;
    private Room currentRoom;

    public Game () {
        gameState = GameState.MAIN;
    }

    private void initRooms() {
        rooms.put("kitchen", new Kitchen());
        rooms.put("hall", new Hall());
    }

    public void initActions() {
        Reflections reflections = new Reflections("orionkropt.game.rooms");
        Set<Class<? extends Room>> subclasses = reflections.getSubTypesOf(Room.class);
        for (Class<? extends Room> subclass : subclasses) {
            System.out.println("Subclass: " + subclass.getName());
            try {
                for (Method m : subclass.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(Command.class)) {
                        Command cmd = m.getAnnotation(Command.class);
                        actions.put(cmd.name(), m);
                        System.out.println("Action: " + cmd.name());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void initialize() {
        initRooms();
        initActions();
        currentRoom = rooms.get(startRoom);
    }

    public void mainLoop (Long id, String message) {
        gameState = gameStates.get(id);
        currentRoom = userRooms.get(id);
        CharacterManager characterManager = new CharacterManager();
        Character currentCharacter = characterManager.getCharacter(id);

        switch (gameState) {
            case MAIN:
                try {
                    Method method = actions.get(message);
                    if (method != null) {
                        actions.get(message).invoke(currentRoom, id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Room room = rooms.get(startRoom);
                if (room != null) {
                    currentRoom = room;
                }
                break;
        }
    }

    public void addUser(Long id) {
        gameStates.put(id, GameState.MAIN);
        userRooms.put(id, rooms.get(startRoom));
    }
}
