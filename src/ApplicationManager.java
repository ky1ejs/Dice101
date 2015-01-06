import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kylejm on 11/12/14.
 */
public class ApplicationManager extends MouseAdapter {

    public static void main(String[] args) {
        final JFrame mainFrame = new JFrame("Dice101");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 500);
        mainFrame.setMinimumSize(new Dimension(500, 500));
        final JPanel initialView = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JLabel informationLabel = new JLabel(
                        "<html>Welcome to Dice101!<br /><br />The aim of this game is to compete against <br />" +
                        "your computer by rolling dice over and<br />" +
                        "over until one of you reaches the target score of 101.<br /><br />" +
                                "Your scores will be saved when the game is closed, and read when<br />" +
                                "launch the game next time." +
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
                    ArrayList<Integer> scores = readScores();
                    final DiceMatViewController diceView = new DiceMatViewController(target, (scores.size() > 0) ? scores.get(0) : 0, (scores.size() > 0) ? scores.get(1) : 0);
                    mainFrame.remove(initialView);
                    mainFrame.add(diceView.getView());
                    mainFrame.revalidate();
                    mainFrame.repaint();
                    mainFrame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            saveScores(diceView.getUserWins(), diceView.getComputerWins());
                        }
                    });
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

    private static void saveScores(int userScore, int computerScore) {
        String stringToSave = String.format("%d:%d", userScore, computerScore);
        try {
            Files.write(Paths.get("./Dice101.scores"), stringToSave.getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static ArrayList<Integer> readScores() {
        ArrayList<Integer> scoreInts = new ArrayList<Integer>();
        String scoresStrings = null;
        File f = new File("./Dice101.scores");
        if (f.exists()) {
            try {
                List<String> lines = Files.readAllLines(Paths.get("./Dice101.scores"));
                scoresStrings = lines.get(0);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (scoresStrings != null) {
                ArrayList<String> strings = new ArrayList<String>(Arrays.asList(scoresStrings.split("\\s*:\\s*")));
                for (int i = 0; i < 2; i++)  scoreInts.add(Integer.parseInt(strings.get(i)));
            }
        }
        return scoreInts;

    }
}
