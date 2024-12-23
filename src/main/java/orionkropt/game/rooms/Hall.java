package orionkropt.game.rooms;

import orionkropt.game.Command;
import orionkropt.game.characters.Character;
import orionkropt.game.characters.CharacterManager;
import orionkropt.game.characters.CharacterStats.Mood;

import java.util.Random;

public class Hall extends Room {
    public Hall() {
        super("hall", "Зал", "src/main/resources/hall.png");
    }

    @Command(name = "playGames", sendName = "Играть")
    public void playGames(Long id) {
        Character character = new CharacterManager().getCharacter(id);
        Random random = new Random();
        character.getStats().changePurity(-15);
        character.getStats().changeSatiety(-15);
        character.getStats().changeEnergy (-30);
        character.getStats().changeHealth(-50);
        character.getStats().changeMood(random.nextInt(2) == 1 ? Mood.HAPPY : Mood.FUNNY);
    }
}
