import javax.swing.*;
import java.awt.*;

/**
 * Created by kylejm on 11/12/14.
 */
public class ApplicationManager {

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Dice101");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(450, 500);
        mainFrame.setMinimumSize(new Dimension(450, 500));
        DiceMatViewController diceView = new DiceMatViewController();
        mainFrame.add(diceView.getView());
        mainFrame.setVisible(true);
    }

}
