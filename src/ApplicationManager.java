import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by kylejm on 11/12/14.
 */
public class ApplicationManager extends MouseAdapter {

    public static void main(String[] args) {
        final JFrame mainFrame = new JFrame("Dice101");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(450, 500);
        mainFrame.setMinimumSize(new Dimension(450, 500));
        final JPanel initialView = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JLabel informationLabel = new JLabel(
                        "<html>Welcome to Dice101!<br /><br />The aim of this game is to compete against <br />" +
                        "your computer by rolling dice over and<br />" +
                        "over until one of you reaches the target score of 101<br /><br />" +
                        "Below you can optionally change the target score</html>");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        initialView.add(informationLabel, c);
        JLabel changeLabel = new JLabel("Target score:");
        c.gridy = 1;
        c.gridwidth = 1;
        initialView.add(changeLabel, c);
        final JTextField targetTextField = new JTextField("101");
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        initialView.add(targetTextField, c);
        JButton startGameButton = new JButton("Start game");
        startGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int target = Integer.parseInt(targetTextField.getText());
                if (target > 0) {
                    mainFrame.setTitle(String.format("Dice%d", target));
                    DiceMatViewController diceView = new DiceMatViewController(target);
                    mainFrame.remove(initialView);
                    mainFrame.add(diceView.getView());
                    mainFrame.revalidate();
                    mainFrame.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a number bigger than 0", "0 or non-number entered", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        c.gridy = 2;
        c.gridx = 0;
        c.gridwidth = 2;
        initialView.add(startGameButton, c);
        mainFrame.add(initialView);
        mainFrame.setVisible(true);
    }
}
