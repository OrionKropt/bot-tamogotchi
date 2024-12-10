package orionkropt.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum ImageManager {
    INSTANCE;

    private static final HashMap<String ,Image> imagePool = new HashMap<>();

    public StatusCode loadImage(Image image) {
        try {
            BufferedImage imageBuffer = ImageIO.read(new File(image.getPath()));
            image.setImage(imageBuffer);
            return StatusCode.IMAGE_LOAD_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return StatusCode.IMAGE_LOAD_ERROR;
        }
    }

    public void initialize() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.json")) {
            JsonNode config = mapper.readTree(input);
            JsonNode characterConfigs = config.get("characters");
            JsonNode roomsConfigs = config.get("rooms");
            characterConfigs.fields().forEachRemaining(entry -> {
                String name = entry.getValue().get("name").asText();
                String path = entry.getValue().get("path").asText();
                if (!Objects.equals(path, "null")) {
                    Image image = new Image(path);
                    loadImage(image);
                    imagePool.put(name, image);
                }
            });
            roomsConfigs.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                Image image = new Image(entry.getValue().asText());
                loadImage(image);
                imagePool.put(key, image);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Image getImage(String name) {
        return imagePool.get(name);
    }
}