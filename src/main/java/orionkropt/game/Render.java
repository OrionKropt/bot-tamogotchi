package orionkropt.game;


import orionkropt.game.rooms.Room;
import orionkropt.image.Image;
import orionkropt.image.ImageManager;
import orionkropt.game.characters.Character;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Render {
    private ImageManager imageManager;
    private File screen;

    public Render() {
        imageManager = ImageManager.INSTANCE;
    }

    public void update(Room room, Character character) {
        String roomName = room.getName();
        String characterType = character.getType();
        Image roomImage = imageManager.getImage(roomName);
        Image characterImage = imageManager.getImage(characterType);
        try {
            BufferedImage bufImageRoom  = ImageIO.read(roomImage.getFile());
            BufferedImage bufImageCharacter = ImageIO.read(characterImage.getFile());
            Point2D position = characterImage.getPosition();
            Graphics2D g2 = bufImageRoom.createGraphics();
            g2.drawImage(bufImageCharacter, (int) position.getX(), (int) position.getY(), null);
            ImageIO.write(bufImageRoom, "png", new File("src/main/resources/screen.png"));
            screen = new File("src/main/resources/screen.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public File release() {
        return screen;
    }
}
