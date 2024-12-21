package orionkropt.game.characters;

public class CharacterStats {
    public static final int STAT_VALUE_MAX = 100;
    public static final int STAT_VALUE_GOOD = 70;
    public static final int STAT_VALUE_POOR = 40;
    public static final int STAT_VALUE_BAD = 10;
    public static final int STAT_VALUE_MIN = 0;

    private int satiety;
    private int purity;
    private int energy;
    private int health;

    public CharacterStats() {
        satiety = STAT_VALUE_MAX;
        purity = STAT_VALUE_MAX;
        energy = STAT_VALUE_MAX;
        health = STAT_VALUE_MAX;
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
}

