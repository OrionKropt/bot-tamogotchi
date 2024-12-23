package orionkropt.game.characters;



public class CharacterStats {
    public static final int STAT_VALUE_MAX = 100;
    public static final int STAT_VALUE_GOOD = 70;
    public static final int STAT_VALUE_POOR = 40;
    public static final int STAT_VALUE_BAD = 10;
    public static final int STAT_VALUE_MIN = 0;

    public enum Mood {HAPPY, FUNNY, SAD, DEPRESSED };

    private int satiety;
    private int purity;
    private int energy;
    private int health;
    // TODO Добавить изображение и анимации
    private Mood mood;

    public CharacterStats() {
        satiety = STAT_VALUE_MAX;
        purity = STAT_VALUE_MAX;
        energy = STAT_VALUE_MAX;
        health = STAT_VALUE_MAX;
        mood = Mood.HAPPY;
    }

    public void changeSatiety(int val) {
        if (satiety + val < STAT_VALUE_MIN) {
            satiety = STAT_VALUE_MIN;
        } else if (satiety + val > STAT_VALUE_MAX) {
            satiety = STAT_VALUE_MAX;
        } else {
            satiety += val;
        }
    }

    public void changePurity(int val) {
        if (purity + val < STAT_VALUE_MIN) {
            purity = STAT_VALUE_MIN;
        } else if (purity + val > STAT_VALUE_MAX) {
            purity = STAT_VALUE_MAX;
        } else {
            purity += val;
        }
    }

    public void changeEnergy(int val) {
        if (energy + val < STAT_VALUE_MIN) {
            energy = STAT_VALUE_MIN;
        } else if (energy + val > STAT_VALUE_MAX) {
            energy = STAT_VALUE_MAX;
        } else {
            energy += val;
        }
    }

    public void changeHealth(int val) {
        if (health + val < STAT_VALUE_MIN) {
            health = STAT_VALUE_MIN;
        } else if (health + val > STAT_VALUE_MAX) {
            health = STAT_VALUE_MAX;
        } else {
            health += val;
        }
    }

    public void changeMood(Mood mood) { this.mood = mood; }

    public int getSatiety() {
        return satiety;
    }

    public int getPurity() {
        return purity;
    }

    public int getEnergy() {
        return energy;
    }

    public int getHealth() {
        return health;
    }

    public Mood getMood() { return mood; }


}

