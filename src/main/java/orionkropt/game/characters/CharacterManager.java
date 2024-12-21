package orionkropt.game.characters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import orionkropt.image.Image;
import orionkropt.image.ImageManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class CharacterManager {
    private static final HashMap<Long, Character> userCharacters = new HashMap<>();
    private static final HashMap<String, Character> characters = new HashMap<>();
    private static final HashMap<String, String> typeToSendingType = new HashMap<>();
    private static Timer time = new Timer();
    private static final Long TIMER_DELAY_ONE_MINUTE = 60000L;

    public void initialize() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.json")) {
            JsonNode config = mapper.readTree(input);
            JsonNode characterConfigs = config.get("characters");
            characterConfigs.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                String name = entry.getValue().get("name").asText();
                String path = entry.getValue().get("path").asText();
                float x = entry.getValue().get("position").get("x").floatValue();
                float y = entry.getValue().get("position").get("y").floatValue();
                Character character = new Character(name, path, x, y);
                if (!Objects.equals(key, "other")) {
                    Image image = character.getImage();
                    image.setImage(ImageManager.INSTANCE.getImage(name).getImage());
                    characters.put(key, character);
                }

            });
            JsonNode typeMappings = config.get("typeToSendingType");
            typeMappings.fields().forEachRemaining(entry -> typeToSendingType.put(entry.getKey(), entry.getValue().asText()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        time.schedule(new TimerTask() {
            @Override
            public void run() {
                for (var character : userCharacters.values()) {
                    character.getStats().changeSatiety(-1);
                }
            }
        }, 0, 15*TIMER_DELAY_ONE_MINUTE);

        time.schedule(new TimerTask() {
            @Override
            public void run() {
                for (var character : userCharacters.values()) {
                    character.getStats().changeEnergy(-1);
                }
            }
        }, 5*TIMER_DELAY_ONE_MINUTE, 15*TIMER_DELAY_ONE_MINUTE);

        time.schedule(new TimerTask() {
            @Override
            public void run() {
                for (var character : userCharacters.values()) {
                    character.getStats().changePurity(-1);
                }
            }
        }, 10*TIMER_DELAY_ONE_MINUTE, 15*TIMER_DELAY_ONE_MINUTE);

        time.schedule(new TimerTask() {
            @Override
            public void run() {
                for (var character : userCharacters.values()) {
                    CharacterStats stats = character.getStats();
                    int change = 0;

                    if (CharacterStats.STAT_VALUE_GOOD <= stats.getSatiety())
                    {
                        change += 1;
                    } else if (stats.getSatiety() <= CharacterStats.STAT_VALUE_POOR) {
                        change -= 1;
                    }

                    if (CharacterStats.STAT_VALUE_GOOD <= stats.getPurity())
                    {
                        change += 1;
                    } else if (stats.getPurity() <= CharacterStats.STAT_VALUE_POOR) {
                        change -= 1;
                    }

                    if (CharacterStats.STAT_VALUE_GOOD <= stats.getEnergy())
                    {
                        change += 1;
                    } else if (stats.getEnergy() <= CharacterStats.STAT_VALUE_POOR) {
                        change -= 1;
                    }

                    stats.changeHealth(change);
                }
            }
        }, 15*TIMER_DELAY_ONE_MINUTE, 15*TIMER_DELAY_ONE_MINUTE);
    }

    public ArrayList<Character> getAllCharacters() {
        ArrayList<Character> listCharacters = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.json")) {
            JsonNode config = mapper.readTree(input);
            JsonNode characterConfigs = config.get("characters");
            characterConfigs.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                if (!Objects.equals(key, "other")) {
                    listCharacters.add(characters.get(key));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listCharacters;
    }

    public StatusCode addCharacter(final Long id, final Character character) {
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

    public StatusCode removeCharacter(final Long id) {
        if (!userCharacters.containsKey(id)) {
            System.out.println("Error: character does not exist");
            return StatusCode.ERROR;
        }
        userCharacters.remove(id);
        return StatusCode.SUCCESS;
    }

    public StatusCode updateCharacter(final Long id, final Character character) {
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

    public Character getCharacter(final Long id) {
        return userCharacters.get(id);
    }

    public Character createCharacter(final String key) {
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

    public String getSendingType(@NotNull Character character) {
        return typeToSendingType.get(character.getType());
    }
}
