package orionkropt.game.rooms;

import orionkropt.game.Command;
import orionkropt.game.characters.Character;
import orionkropt.game.characters.CharacterManager;

public class Bedroom extends Room {
    public Bedroom() {
        super("bedroom", "Спальня", "src/main/resources/bedroom.png");
    }

    @Command(name = "sleep", sendName = "Уложить спать")
    public void sleep(Long id) {
        Character character = new CharacterManager().getCharacter(id);
        character.getStats().changeEnergy(100);
        character.getStats().changeHealth(100);
        character.getStats().changeSatiety(-60);
        character.getStats().changePurity(-20);
    }

}

