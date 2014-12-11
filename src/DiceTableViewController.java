import javax.swing.*;

/**
 * Created by kylejm on 11/12/14.
 */
public class DiceTableViewController {
    private JPanel view;

    public DiceTableViewController() {
        view.setSize(300, 300);
        view.setVisible(true);
    }

    public JPanel getPanel1() {
        return view;
    }
}
