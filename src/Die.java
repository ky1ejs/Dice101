import javax.swing.*;

/**
 * Created by kylejm on 31/12/14.
 * //This class should be called DieFace, but the coursework spec specifies that it must be called Die
 */
public class Die implements DieIntf, Comparable<Die> {
    ImageIcon image;
    int value;

    @Override
    public ImageIcon getDieImage() {
        return null;
    }

    @Override
    public void setImage(ImageIcon image) {
        if (value > 0 && value < 7) this.image = new ImageIcon(String.format("Assets/dice-%f", value));
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int value) {
        if (value > 0 && value < 7) {
            this.value = value;
            setImage(null);
        }
    }

    @Override
    public int compareTo(Die o) {
        if (o.getValue() < value) {
            return -1;
        } else if (o.getValue() > value) {
            return 1;
        } else {
            return 0;
        }
    }
}
