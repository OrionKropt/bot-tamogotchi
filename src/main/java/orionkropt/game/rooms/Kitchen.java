package orionkropt.game.rooms;

import orionkropt.game.Command;
import orionkropt.game.characters.Character;
import orionkropt.game.characters.CharacterManager;

import java.util.Random;

public class Kitchen extends Room {
    public Kitchen() {
        super("kitchen", "Кухня", "src/main/resources/kitchen.png");
    }

    @Command(name = "feed", sendName = "Кормить")
    public void feed(Long id) {
        Random rand = new Random();
        Character character = new CharacterManager().getCharacter(id);
        character.getStats().changeSatiety(100);
        character.getStats().changePurity(-1 * rand.nextInt(50));
    }

    @Command(name = "cook", sendName = "Готовить")
    public void cook(Long id) {
        Random rand = new Random();
        Character character = new CharacterManager().getCharacter(id);
        character.getStats().changeEnergy(-20);
        character.getStats().changePurity(-1 * rand.nextInt(50));
    }
}
