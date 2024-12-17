package orionkropt.image;


import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.geom.Point2D;

public class Image {
    BufferedImage img;
    String path;
    File file;
    Point2D position;

    public Image(String path) {
        this.path = path;
        file = new File(path);
        position = new Point2D.Float(0, 0);
        img = null;
    }

    public Image(String path, float x, float y) {
        this.path = path;
        file = new File(path);
        position = new Point2D.Float(x, y);
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

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        position = new Point2D.Float(x, y);
    }
}
