package orionkropt.characters;


import java.util.HashMap;

public class CharacterManager {
    private static final HashMap<Long, Character> userCharacters = new HashMap<>();
    private static final HashMap<String, Character> characters = new HashMap<>();
    private static final HashMap<String, String> typeToSendingType = new HashMap<>();

    CharacterManager() {
        characters.put("pig_selected", new Character("pig"));
        characters.put("other_selected", null);

        typeToSendingType.put("pig", "Свинка");
    }

    StatusCode addCharacter(final Long id, final Character character) {
        if (character == null) {
            System.out.println("Error: character is null");
            return StatusCode.ERROR;
        }
        if (userCharacters.containsKey(id)) {
            System.out.println("Error: character already exists");
            return StatusCode.ERROR;
        }
        userCharacters.put(id, character);
        return StatusCode.SUCCESS;
    }

    StatusCode removeCharacter(final Long id) {
        if (!userCharacters.containsKey(id)) {
            System.out.println("Error: character does not exist");
            return StatusCode.ERROR;
        }
        userCharacters.remove(id);
        return StatusCode.SUCCESS;
    }

    StatusCode updateCharacter(final Long id, final Character character) {
        if (character == null) {
            System.out.println("Error: character is null");
            return StatusCode.ERROR;
        }
        if (!userCharacters.containsKey(id)) {
            System.out.println("Error: character does not exist");
            return StatusCode.ERROR;
        }
        userCharacters.put(id, character);
        return StatusCode.SUCCESS;
    }

    Character getCharacter(final Long id) {
        return userCharacters.get(id);
    }

    Character createCharacter(final String key) {
        if (key == null) {
            System.out.println("Error: character key is null");
            return null;
        }
        if (!characters.containsKey(key)) {
            System.out.println("Error: character does not exist");
            return null;
        }
        return characters.get(key);
    }

    String getSendingType(Character character) {
        return typeToSendingType.get(character.getType());
    }
}
