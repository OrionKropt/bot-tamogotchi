package orionkropt.game;

import org.reflections.Reflections;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import orionkropt.bot.InlineKeyboard;
import orionkropt.collections.Pair;
import orionkropt.game.characters.Character;
import orionkropt.game.characters.CharacterManager;
import orionkropt.game.rooms.Bedroom;
import orionkropt.game.rooms.Hall;
import orionkropt.game.rooms.Kitchen;
import orionkropt.game.rooms.Room;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

enum GameState {
    MAIN,
    ROOM_SELECTION
}

public class Game {
    static private final HashMap<Long, GameState> gameStates = new HashMap<>();
    static private final HashMap<Long, Room> userRooms = new HashMap<>();
    static private final HashMap<String, Room> rooms = new HashMap<>();
    static private final HashMap<String, Method> roomActions = new HashMap<>();
    static private final HashMap<String, Method> gameActions = new HashMap<>();
    static private final Render render = new Render();
    static private final String startRoom = "bedroom";
    private GameState gameState;
    private Room currentRoom;
    CharacterManager characterManager;

    public Game () {
        gameState = GameState.MAIN;
        characterManager = new CharacterManager();
    }

    private void initRooms() {
        rooms.put("kitchen", new Kitchen());
        rooms.put("hall", new Hall());
        rooms.put("bedroom", new Bedroom());
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
                        roomActions.put(cmd.name(), m);
                        System.out.println("Action: " + cmd.name());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Action.class)) {
                Action a = m.getAnnotation(Action.class);
                gameActions.put(a.name(), m);
                System.out.println("Action: " + a.name());
            }
        }
    }

    public void initialize() {
        initRooms();
        initActions();
    }

    public void startGame(Long id, SendPhoto sendPhoto, InlineKeyboard inlineKeyboard) {
        gameStates.put(id, gameState);
        userRooms.put(id, rooms.get(startRoom));
        render.update(rooms.get(startRoom), characterManager.getCharacter(id));
        sendPhoto.setChatId(id.toString());
        sendPhoto.setPhoto(new InputFile(render.release()));
        setMainKeyboard(rooms.get(startRoom), inlineKeyboard);
    }

    public void mainLoop (Long id, String message,  SendPhoto sp, InlineKeyboard inlineKeyboard) {
        gameState = gameStates.get(id);
        currentRoom = userRooms.get(id);
        Character currentCharacter = characterManager.getCharacter(id);

        if (currentCharacter == null) {
            System.out.println("Character not found");
            return;
        }

        switch (gameState) {
            case MAIN:
                runRoomAction(message, id, inlineKeyboard);
                runGameAction(message, id, inlineKeyboard);
                break;
            case ROOM_SELECTION:
                currentRoom = rooms.get(message);
                setMainKeyboard(currentRoom, inlineKeyboard);
                gameState = GameState.MAIN;
                break;
        }

        render.update(currentRoom, currentCharacter);
        sp.setChatId(id.toString());
        sp.setPhoto(new InputFile(render.release()));
        sp.setReplyMarkup(inlineKeyboard.getInlineKeyboardMarkup());
        gameStates.put(id, gameState);
        userRooms.put(id, currentRoom);
    }

    private void setMainKeyboard(Room currentRoom, InlineKeyboard inlineKeyboard) {
        setCurrentRoomActionsOnScreen(currentRoom, inlineKeyboard);
        setRoomChangeButton(inlineKeyboard);
    }

    private void setCurrentRoomActionsOnScreen(Room room, InlineKeyboard inlineKeyboard) {
        inlineKeyboard.clear();
        ArrayList<Pair<String, String>> rawKeyboardData = new ArrayList<>();
        try {
            for (Method m : room.getClass().getDeclaredMethods()) {
                if (m.isAnnotationPresent(Command.class)) {
                    Command cmd = m.getAnnotation(Command.class);
                    rawKeyboardData.add(new Pair<>(cmd.sendName(), cmd.name()));
                }
            }
            inlineKeyboard.createInlineKeyboardMarkup(rawKeyboardData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRoomChangeButton(InlineKeyboard inlineKeyboard) {
        inlineKeyboard.appendButton(new Pair<>("Сменить комнату", "changeRoom"));
    }

    private void setBackButton(InlineKeyboard inlineKeyboard) {
        inlineKeyboard.appendButton(new Pair<>("Назад", "back"));
    }

    private void setRoomListKeyboardOnScreen(InlineKeyboard inlineKeyboard) {
        inlineKeyboard.clear();
        ArrayList<Pair<String, String>> rawKeyboardData = new ArrayList<>();
        for (Room room : rooms.values()) {
            rawKeyboardData.add(new Pair<>(room.getSendName(), room.getName()));
        }
        inlineKeyboard.createInlineKeyboardMarkup(rawKeyboardData);
    }

    private void runRoomAction(String action, Long id,InlineKeyboard inlineKeyboard) {
        Method method = roomActions.get(action);
        try {
            if (method != null) {
                method.invoke(currentRoom, id);
                setMainKeyboard(currentRoom, inlineKeyboard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runGameAction(String action, Long id, InlineKeyboard inlineKeyboard) {
        Method method = gameActions.get(action);
        try {
            if (method != null) {
                method.invoke(this, inlineKeyboard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Action(name = "changeRoom")
    private void clickedСhangeRoom(InlineKeyboard inlineKeyboard) {
        setRoomListKeyboardOnScreen(inlineKeyboard);
        setBackButton(inlineKeyboard);
        gameState = GameState.ROOM_SELECTION;
    }
    @Action(name = "back")
    private void clickedBack(InlineKeyboard inlineKeyboard) {
        setCurrentRoomActionsOnScreen(currentRoom, inlineKeyboard);
        gameState = GameState.MAIN;
    }
}
