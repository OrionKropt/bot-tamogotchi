package orionkropt.characters;

public class Character {
    private String name;
    private String type;
    private int satiety;
    private int purity;
    private int health;
    private Mood mood;

    public enum Mood {HAPPY, FUNNY, SAD, DEPRESSED}
    // ToDo Добавить изображение и анимации

    Character(String type) {
        this.type = type;
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
}

