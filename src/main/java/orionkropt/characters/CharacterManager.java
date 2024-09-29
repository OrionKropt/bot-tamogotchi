package orionkropt.characters;


import java.util.HashMap;
import java.util.Map;

public class CharacterManager {
    private static final Map<Long, Character> characters = new HashMap<>();

    StatusCode addCharacter(final Long id, final Character character) {
        if (character == null) {
            System.out.println("Error: character is null");
            return StatusCode.ERROR;
        }
        if (characters.containsKey(id)) {
            System.out.println("Error: character already exists");
            return StatusCode.ERROR;
        }
        characters.put(id, character);
        return StatusCode.SUCCESS;
    }

    StatusCode removeCharacter(final Long id) {
        if (!characters.containsKey(id)) {
            System.out.println("Error: character does not exist");
            return StatusCode.ERROR;
        }
        characters.remove(id);
        return StatusCode.SUCCESS;
    }

    StatusCode updateCharacter(final Long id, final Character character) {
        if (character == null) {
            System.out.println("Error: character is null");
            return StatusCode.ERROR;
        }
        if (!characters.containsKey(id)) {
            System.out.println("Error: character does not exist");
            return StatusCode.ERROR;
        }
        characters.put(id, character);
        return StatusCode.SUCCESS;
    }

    Character getCharacter(final Long id) {
        return characters.get(id);
    }
}
