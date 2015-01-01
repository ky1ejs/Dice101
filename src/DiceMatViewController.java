import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by kylejm on 11/12/14.
 */
public class DiceMatViewController extends MouseAdapter {
    private JPanel view;
    private JButton throwButton;
    private Dice dice;
    private ArrayList<ImageLabel> userImageLabels;


    public DiceMatViewController() {
        throwButton.addMouseListener(this);
        dice = new Dice();
        view.setSize(300, 300);
        userImageLabels = new ArrayList<ImageLabel>();
        for (int i = 0; i < 5; i++) {
            ImageLabel label = new ImageLabel(300, 300);
            view.add(label);
            userImageLabels.add(label);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throwButton.setEnabled(false);
        ArrayList<Die> results = dice.roll();
        for (int i = 0; i < 5; i++) {
            Die face = results.get(i);
            ImageLabel label = userImageLabels.get(i);
            label.setImage(face.getDieImage().getImage());
        }
        throwButton.setEnabled(true);
    }

    public JPanel getView() {
        return view;
    }
}
