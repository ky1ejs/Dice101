import javax.swing.*;
import java.awt.*;

/**
 * Created by kylejm on 16/12/14.
 */
public class ImageLabel extends JLabel {
    private Image originalImage;

    public ImageLabel(Image image, int width, int height) {
        this.originalImage = image;
        Image scaledImage = this.originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        this.setIcon(new ImageIcon(scaledImage));
        this.setPreferredSize(new Dimension(width, height));
    }
}