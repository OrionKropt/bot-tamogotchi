package orionkropt.game.rooms;

import orionkropt.game.Command;
import orionkropt.game.characters.Character;
import orionkropt.game.characters.CharacterManager;

public class Bathroom extends Room {
    public Bathroom() {
        super("bathroom", "Ванная", "src/main/resources/bathroom.png");
    }

    @Command(name = "wash", sendName = "Помыться")
    public void wash(Long id) {
        Character character = new CharacterManager().getCharacter(id);
        character.getStats().changePurity(100);
    }
}
