package orionkropt.image;

import java.awt.image.BufferedImage;
import java.io.File;

public class Image {
    BufferedImage img;
    String path;
    File file;
    public Image(String path) {
        this.path = path;
        file = new File(path);
        img = null;
    }

    public String getPath() {
        return path;
    }

    public File getFile() { return file; }

    public void setImage(BufferedImage img) {
        this.img = img;
    }

    public BufferedImage getImage() {
        return img;
    }

}
