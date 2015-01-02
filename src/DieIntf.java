import javax.swing.*;

/**
 * Created by kylejm on 30/12/14.
 */
public interface DieIntf {
    public ImageIcon getDieImage(); //This should be getDieImageIcon, but the coursework spec specifies that it should be named this
    public void setImage(ImageIcon image);
    public int getValue();
    public void setValue(int value);
}
