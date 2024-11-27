package orionkropt.game.rooms;

import orionkropt.image.Image;
import java.util.ArrayList;

public abstract class Room {
    private Image image;
    private String name;

    protected ArrayList<String> actions;

    public Room(String name, String path) {
        this.name = name;
        this.image = new Image(path);
    }

    public Image getImage(){
        return image;
    }

    public String getName() {
        return name;
    }
}
