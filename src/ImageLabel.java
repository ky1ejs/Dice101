import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by kylejm on 16/12/14.
 */
public class ImageLabel extends JLabel implements ComponentListener {
    private Image originalImage;

    public ImageLabel(int initialWidth, int initialHeight) {
        this.setPreferredSize(new Dimension(initialWidth, initialHeight));
        this.addComponentListener(this);
    }

    public ImageLabel(int initialWidth, int initialHeight, Image image) {
        this.setPreferredSize(new Dimension(initialWidth, initialHeight));
        setImage(image);
        this.addComponentListener(this);
    }

    public void setImage(Image image){
        this.originalImage = image;
        scaleAndSetImage(this.originalImage);
    }

    private void scaleAndSetImage(Image image) {
        //If statements to maintain square aspect ration. Should make this functionality work when the ImageLabel is in "AspectFit" mode.
        if (getWidth() > 0 && getHeight() > 0) {
            int width, height;
            if (getWidth() == getHeight()) {
                width = getWidth();
                height = getHeight();
            } else if (getWidth() < getHeight()) {
                width = getWidth();
                height = getWidth();
            } else {
                width = getHeight();
                height = getHeight();
            }
            Image scaledImage = this.originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            this.setIcon(new ImageIcon(scaledImage));
        }
    }

    private void refreshImageIcon() {
        scaleAndSetImage(originalImage);
        revalidate();
        repaint();
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        refreshImageIcon();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        refreshImageIcon();
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }
}