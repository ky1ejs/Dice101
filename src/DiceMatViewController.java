import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by kylejm on 11/12/14.
 */
public class DiceMatViewController extends MouseAdapter {
    private JPanel view = new JPanel(new GridBagLayout());
    private GridBagConstraints gridConstraints = new GridBagConstraints();
    private JButton throwButton = new JButton("Throw");
    private JButton scoreButton = new JButton("Score");
    private Dice userDice = new Dice();
    private Dice computerDice = new Dice();
    private int gameStatRows = 2;
    private int gameStatCols = 3;
    private JPanel[][] userStatPanels = new JPanel[gameStatRows][gameStatCols];
    private JPanel[][] computerStatPanels = new JPanel[gameStatRows][gameStatCols];
    private ArrayList<ImageLabel> userImageLabels = new ArrayList<ImageLabel>();
    private ArrayList<ImageLabel> computerImageLabels = new ArrayList<ImageLabel>();
    private JLabel userScoreLabel = new JLabel();
    private JLabel computerScoreLabel = new JLabel();
    private int rollCount = 0;
    private int userScore = 0;
    private int computerScore = 0;


    public DiceMatViewController() {
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.weightx = 1;
        gridConstraints.weighty = 1;
        initGUI();
    }

    private void initGUI() {
        initGameStatGUI();
        initDiceGUI();
        initButtons();
    }

    private void initGameStatGUI() {
        JPanel userStatsGrid = new JPanel(new GridLayout(gameStatRows, gameStatCols));
        JPanel computerStatsGrid = new JPanel(new GridLayout(gameStatRows, gameStatCols));
        for (int row = 0; row < gameStatRows; row++) {
            for (int col = 0; col < gameStatCols; col++) {
                userStatPanels[row][col] = new JPanel();
                userStatsGrid.add(userStatPanels[row][col]);
                computerStatPanels[row][col] = new JPanel();
                computerStatsGrid.add(computerStatPanels[row][col]);
            }
        }
        userStatPanels[0][1].add(new JLabel("User")); //TODO: Make labels show centred over each set of dice
        computerStatPanels[0][1].add(new JLabel("Computer"));
        userStatPanels[1][0].add(new JLabel("Score:"));
        computerStatPanels[1][0].add(new JLabel("Score:"));
        userStatPanels[1][1].add(userScoreLabel);
        computerStatPanels[1][1].add(computerScoreLabel);
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 0;
        view.add(userStatsGrid, gridConstraints);
        gridConstraints.gridx = 1;
        view.add(computerStatsGrid, gridConstraints);
    }

    private void initDiceGUI() {
        GridLayout diceLayout = new GridLayout(3, 2);
        JPanel userDiceView = new JPanel(diceLayout);
        JPanel computerDiceView = new JPanel(diceLayout);
        for (int i = 0; i < 5; i++) {
            ImageLabel label = new ImageLabel(100, 100, userDice.getBlankFace().getImage());
            userDiceView.add(label);
            userImageLabels.add(label);
            label = new ImageLabel(100, 100, computerDice.getBlankFace().getImage());
            computerDiceView.add(label);
            computerImageLabels.add(label);
        }
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 1;
        view.add(userDiceView, gridConstraints);
        gridConstraints.gridx = 1;
        view.add(computerDiceView, gridConstraints);
    }

    private void initButtons() {
        throwButton.addMouseListener(this);
        scoreButton.addMouseListener(this);
        gridConstraints.gridx = 0;
        gridConstraints.gridy = 2;
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;
        view.add(throwButton, gridConstraints);
        gridConstraints.gridx = 1;
        view.add(scoreButton, gridConstraints);
        gridConstraints.fill = GridBagConstraints.BOTH;
        scoreButton.setEnabled(false);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getComponent() == throwButton) {
            throwButton.setEnabled(false);
            ArrayList<Die> userResults = userDice.roll();
            ArrayList<Die> computerResults = computerDice.roll();
            for (int i = 0; i < 5; i++) {
                Die face = userResults.get(i);
                ImageLabel label = userImageLabels.get(i);
                label.setImage(face.getDieImage().getImage());
                face = computerResults.get(i);
                label = computerImageLabels.get(i);
                label.setImage(face.getDieImage().getImage());
            }
            throwButton.setEnabled(true);
            rollCount++;
            if (rollCount == 1) {
                throwButton.setText("Re-roll");
                scoreButton.setEnabled(true);
            } else if (rollCount == 4) scoreDice();
        } else {
            scoreDice();
        }
    }

    private void scoreDice() {
        userScore += userDice.scoreLatestResult();
        computerScore += computerDice.scoreLatestResult();
        userScoreLabel.setText(String.format("%d", userScore));
        computerScoreLabel.setText(String.format("%d", computerScore));
        throwButton.setText("Throw");
        scoreButton.setEnabled(false);
        rollCount = 0;
    }

    public JPanel getView() {
        return view;
    }
}
