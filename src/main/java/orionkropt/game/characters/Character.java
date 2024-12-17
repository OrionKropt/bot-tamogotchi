package orionkropt.game.characters;
import orionkropt.image.Image;

public class Character {
    private String name;
    private String type;
    private int satiety;
    private int purity;
    private int health;
    private Mood mood;
    private Image image;

    public enum Mood {HAPPY, FUNNY, SAD, DEPRESSED}
    // ToDo Добавить изображение и анимации

    public Character(String type, String path, float x, float y) {
        this.type = type;
        image = new Image(path, x, y);
        name = "default";
        satiety = 100;
        purity = 100;
        health = 100;
        mood = Mood.HAPPY;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getSatiety() {
        return satiety;
    }

    public int getPurity() {
        return purity;
    }

    public int getHealth() {
        return health;
    }

    public Mood getMood() {
        return mood;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() { return this.image; }
}

