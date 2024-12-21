package orionkropt.game.rooms;

import orionkropt.image.Image;
import java.util.ArrayList;

public abstract class Room {
    private Image image;
    private String name;
    private  String sendName;

    protected ArrayList<String> actions;

    public Room(String name, String sendName, String path) {
        this.name = name;
        this.sendName = sendName;
        this.image = new Image(path);
    }

    public Image getImage(){
        return image;
    }

    public String getSendName() {
        return sendName;
    }

    public String getName() {
        return name;
    }
}
