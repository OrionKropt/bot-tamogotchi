package orionkropt.game;


import orionkropt.game.rooms.Room;
import orionkropt.image.Image;
import orionkropt.image.ImageManager;
import orionkropt.game.characters.Character;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Render {
    private ImageManager imageManager;
    private Image screen;

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
            Graphics2D g2 = bufImageRoom.createGraphics();
            g2.drawImage(bufImageCharacter,  590,450, null);
            ImageIO.write(bufImageRoom, "png", new File("src/main/resources/a.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }





    }



}
