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
    private JButton scoreButton = new JButton("Score");
    private Dice dice = new Dice();
    private int gameStatRows = 2;
    private int gameStatCols = 3;
    private JPanel[][] userStatContainers = new JPanel[gameStatRows][gameStatCols];
    private JPanel[][] computerStatContainers = new JPanel[gameStatRows][gameStatCols];
    private ArrayList<ImageLabel> userImageLabels = new ArrayList<ImageLabel>();
    private ArrayList<ImageLabel> computerImageLabels = new ArrayList<ImageLabel>();
    private JLabel userScoreLabel = new JLabel();
    private JLabel computerScoreLabel = new JLabel();


    public DiceMatViewController() {
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
                userStatContainers[row][col] = new JPanel();
                userStatsGrid.add(userStatContainers[row][col]);
                computerStatContainers[row][col] = new JPanel();
                computerStatsGrid.add(computerStatContainers[row][col]);
            }
        }
        userStatContainers[0][1].add(new JLabel("User")); //TODO: Make labels show centred over each set of dice
        computerStatContainers[0][1].add(new JLabel("Computer"));
        userStatContainers[1][0].add(new JLabel("Score:"));
        computerStatContainers[1][0].add(new JLabel("Score:"));
        userStatContainers[1][1].add(userScoreLabel);
        computerStatContainers[1][1].add(computerScoreLabel);
        JPanel statGridContainer = new JPanel(new BorderLayout());
        statGridContainer.add(userStatsGrid, BorderLayout.LINE_START);
        statGridContainer.add(computerStatsGrid, BorderLayout.LINE_END);
        view.add(statGridContainer, BorderLayout.PAGE_START);
    }

    private void initDiceGUI() {
        GridLayout diceLayout = new GridLayout(3, 2);
        JPanel userDice = new JPanel(diceLayout);
        JPanel computerDice = new JPanel(diceLayout);
        for (int i = 0; i < 5; i++) {
            ImageLabel label = new ImageLabel(dice.getBlankFace().getImage(), 100, 100);
            userDice.add(label);
            userImageLabels.add(label);
            label = new ImageLabel(dice.getBlankFace().getImage(), 100, 100);
            computerDice.add(label);
            computerImageLabels.add(label);
        }
        view.add(userDice, BorderLayout.LINE_START);
        view.add(computerDice, BorderLayout.LINE_END);
    }

    private void initButtons() {
        JPanel buttonContainer = new JPanel(new BorderLayout());
        throwButton.addMouseListener(this);
        scoreButton.addMouseListener(this);
        buttonContainer.add(throwButton, BorderLayout.LINE_START);
        buttonContainer.add(scoreButton, BorderLayout.LINE_END);
        view.add(buttonContainer, BorderLayout.PAGE_END);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getComponent() == throwButton) {
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
        } else {
            //TODO: Add score logic
        }
    }

    public JPanel getView() {
        return view;
    }
}
