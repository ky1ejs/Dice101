import javax.swing.*;
import java.awt.*;

/**
 * Created by kylejm on 16/12/14.
 */
public class ImageLabel extends JLabel {
    private Image originalImage;

    public ImageLabel(int width, int height) {
        this.setSize(new Dimension(width, height));
    }

    public ImageLabel(Image image, int width, int height) {
        this.setSize(new Dimension(width, height));
        setImage(image);
    }

    public void setImage(Image image){
        this.originalImage = image;
        Image scaledImage = this.originalImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        this.setIcon(new ImageIcon(scaledImage));
    }
}