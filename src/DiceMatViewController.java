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
    private int gameStatRows = 2;
    private int gameStatCols = 3;
    private JPanel[][] userStatContainers = new JPanel[gameStatRows][gameStatCols];
    private JPanel[][] computerStatContainers = new JPanel[gameStatRows][gameStatCols];
    private ArrayList<ImageLabel> userImageLabels = new ArrayList<ImageLabel>();
    private ArrayList<ImageLabel> computerImageLabels = new ArrayList<ImageLabel>();


    public DiceMatViewController() {
        initGUI();
    }

    private void initGUI() {
        initGameStatGUI();
        initDiceGUI();
        throwButton.addMouseListener(this);
        view.add(throwButton, BorderLayout.PAGE_END);
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
        userStatContainers[1][1].add(new JLabel("User")); //TODO: Make labels show centred over each set of dice
        computerStatContainers[1][1].add(new JLabel("Computer"));
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
