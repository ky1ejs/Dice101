import javax.swing.*;

/**
 * Created by kylejm on 11/12/14.
 */
public class ApplicationManager {

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Dice101");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 600);
        DiceMatViewController diceView = new DiceMatViewController();
        mainFrame.add(diceView.getPanel1());
        mainFrame.setVisible(true);
    }

}
