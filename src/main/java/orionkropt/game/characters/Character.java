package orionkropt.game.characters;
import orionkropt.image.Image;

public class Character {
    private String name;
    private String type;
    private CharacterStats stats;
    private Mood mood;
    private Image image;

    public enum Mood {HAPPY, FUNNY, SAD, DEPRESSED}
    // ToDo Добавить изображение и анимации

    public Character(String type, String path, float x, float y) {
        this.type = type;
        image = new Image(path, x, y);
        name = "default";
        stats = new CharacterStats();
        mood = Mood.HAPPY;
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

    public Mood getMood() {
        return mood;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() { return this.image; }
}

