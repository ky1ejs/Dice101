import javax.swing.*;

/**
 * Created by kylejm on 11/12/14.
 */
public class DiceMatViewController {
    private JPanel view;
    private JLabel dice1;

    public DiceMatViewController() {
        view.setSize(300, 300);
        ImageIcon image = new ImageIcon("Assets/Dice.png");
        dice1.setIcon(image);
    }

    public JPanel getPanel1() {
        return view;
    }
}