package orionkropt.characters;

public class Character {
    private String name;
    private int satiety;
    private int purity;
    private int health;
    private Mood mood;

    public enum Mood {HAPPY, FUNNY, SAD, DEPRESSED}
    // ToDo Добавить изображение и анимации

    Character(String name) {
        this.name = name;
        satiety = 100;
        purity = 100;
        health = 100;
        mood = Mood.HAPPY;
    }

    public String getName() {
        return name;
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
}

