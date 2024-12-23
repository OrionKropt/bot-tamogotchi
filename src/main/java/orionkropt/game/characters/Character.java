package orionkropt.game.characters;
import orionkropt.image.Image;

public class Character {
    private String name;
    private String type;
    private CharacterStats stats;
    private Image image;

    public Character(String type, String path, float x, float y) {
        this.type = type;
        image = new Image(path, x, y);
        name = "default";
        stats = new CharacterStats();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public CharacterStats getStats() {
        return stats;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() { return this.image; }
}

