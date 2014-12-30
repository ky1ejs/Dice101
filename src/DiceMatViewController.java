import javax.swing.*;

/**
 * Created by kylejm on 11/12/14.
 */
public class DiceMatViewController {
    private JPanel view;

    public DiceMatViewController() {
        view.setSize(300, 300);
        ImageIcon imageIcon = new ImageIcon("Assets/Dice.png");
        ImageLabel imageLabel = new ImageLabel(imageIcon.getImage(), 100, 100);
        view.add(imageLabel);
    }

    public JPanel getPanel1() {
        return view;
    }
}
