import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by kylejm on 11/12/14.
 */
public class DiceMatViewController extends MouseAdapter {
    private JPanel view = new JPanel(new BorderLayout());
    private JButton throwButton = new JButton("Throw");
    private Dice dice = new Dice();
    private ArrayList<ImageLabel> userImageLabels = new ArrayList<ImageLabel>();
    private ArrayList<ImageLabel> computerImageLabels = new ArrayList<ImageLabel>();


    public DiceMatViewController() {
        GridLayout diceLayout = new GridLayout(3, 2);
        JPanel userDice = new JPanel(diceLayout);
        JPanel computerDice = new JPanel(diceLayout);
        throwButton.addMouseListener(this);
        view.setSize(300, 300);
        for (int i = 0; i < 5; i++) {
            ImageLabel label = new ImageLabel(dice.getBlankFace().getImage(), 200, 200);
            userDice.add(label);
            userImageLabels.add(label);
        }
        for (int i = 0; i < 5; i++) {
            ImageLabel label = new ImageLabel(dice.getBlankFace().getImage(), 200, 200);
            computerDice.add(label);
            computerImageLabels.add(label);
        }
        view.add(userDice, BorderLayout.LINE_START);
        view.add(computerDice, BorderLayout.LINE_END);
        view.add(throwButton, BorderLayout.PAGE_END);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throwButton.setEnabled(false);
        ArrayList<Die> userResults = dice.roll();
        ArrayList<Die> computerResults = dice.roll();
        for (int i = 0; i < 5; i++) {
            Die face = userResults.get(i);
            ImageLabel label = userImageLabels.get(i);
            label.setImage(face.getDieImage().getImage());
            face = computerResults.get(i);
            label = computerImageLabels.get(i);
            label.setImage(face.getDieImage().getImage());
        }
        throwButton.setEnabled(true);
    }

    public JPanel getView() {
        return view;
    }
}
