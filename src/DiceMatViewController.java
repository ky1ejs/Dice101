import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private ArrayList<ImageLabel> userImageLabels = new ArrayList<ImageLabel>();
    private ArrayList<ImageLabel> computerImageLabels = new ArrayList<ImageLabel>();
    private JLabel userScoreLabel = new JLabel("    ");
    private JLabel computerScoreLabel = new JLabel("    ");
    private JLabel userRollCountLabel = new JLabel("    ");
    private JLabel computerRollCountLabel = new JLabel("    ");
    private int userScore = 0;
    private int computerScore = 0;
    private int userRollCount = 0;
    private int computerRollCount = 0;
    private final int diceHoldDelay = 2500;
    private final TimeUnit delayUnit = TimeUnit.MILLISECONDS;


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
        JPanel userStatsGrid = new JPanel(new GridBagLayout());
        JPanel computerStatsGrid = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 2;
        constraints.gridy = 0;
        userStatsGrid.add(new JLabel("User"), constraints);
        computerStatsGrid.add(new JLabel("Computer"), constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        userStatsGrid.add(new JLabel("Score:"), constraints);
        computerStatsGrid.add(new JLabel("Score:"), constraints);
        constraints.gridx = 1;
        userStatsGrid.add(userScoreLabel, constraints);
        computerStatsGrid.add(computerScoreLabel, constraints);
        constraints.gridx = 3;
        userStatsGrid.add(new JLabel("Rolls left:"), constraints);
        computerStatsGrid.add(new JLabel("Rolls left:"), constraints);
        constraints.gridx = 4;
        userStatsGrid.add(userRollCountLabel, constraints);
        computerStatsGrid.add(computerRollCountLabel, constraints);
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
            label.addMouseListener(this);
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
        setImageLabelsEnabled(false);
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

    private void setImageLabelsEnabled(boolean enabled) {
        for (ImageLabel label : userImageLabels) {
            label.setEnabled(enabled);
        }
        for (ImageLabel label : computerImageLabels) {
            label.setEnabled(enabled);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getComponent() == throwButton && throwButton.isEnabled()) {
            rollDice();
        } else if (e.getComponent() == scoreButton && scoreButton.isEnabled()) {
            scoreDice();
        } else if (e.getComponent().isEnabled()) {
            ImageLabel selectedLabel = (ImageLabel) e.getComponent();
            if (userImageLabels.contains(selectedLabel)) {
                int index = userImageLabels.indexOf(selectedLabel);
                selectedLabel.setImage(userDice.toggleSelectionOfDieAtIndex(index).getImage());
            }
        }
    }

    private void rollDice() {
        if (userRollCount == 0) setImageLabelsEnabled(true);
        throwButton.setEnabled(false);
        scoreButton.setEnabled(false);
        rollUserDice();
        rollComputerDice();
    }

    private void rollUserDice() {
        Runnable task =  new Runnable() {
            @Override
            public void run() {
                ArrayList<Die> results = userDice.roll();
                int i = 0;
                for (Die face : results) {
                    ImageLabel label = userImageLabels.get(i);
                    label.setImage(face.getDieImage().getImage());
                    i++;
                }
                userDice.resetSelections();
                userRollCount++;
                userRollCountLabel.setText(String.format("%d", 3 - userRollCount));
            }
        };
        throwButton.setText(String.format("You're holding %d dice", userDice.selectionCount()));
        if (userRollCount > 0) {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(task, diceHoldDelay, delayUnit);
        } else {
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    private void rollComputerDice() {
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                if (computerRollCount > 0) {
                    Random numGen = new Random();
                    int qtyOfDiceToHold = numGen.nextInt(6); //from [0, 7) because there are 5 die faces and the option to not hold
                    if (qtyOfDiceToHold > 0) {
                        ArrayList<Integer> indexes = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4));
                        for (int i = 0; i < qtyOfDiceToHold; i++) {
                            int indexToSelect = numGen.nextInt(indexes.size());
                            ImageLabel label = computerImageLabels.get(indexes.get(indexToSelect));
                            label.setImage(computerDice.toggleSelectionOfDieAtIndex(indexes.get(indexToSelect)).getImage());
                            indexes.remove(indexToSelect);
                        }
                    }
                    scoreButton.setText(String.format("Computer holding %d dice", qtyOfDiceToHold));
                }
                Runnable rollDice = new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Die> results = computerDice.roll();
                        int i = 0;
                        for (Die face : results) {
                            ImageLabel label = computerImageLabels.get(i);
                            label.setImage(face.getDieImage().getImage());
                            i++;
                        }
                        computerDice.resetSelections();
                        computerRollCount++;
                        computerRollCountLabel.setText(String.format("%d", 3 - computerRollCount));
                        throwButton.setText("Re-roll");
                        scoreButton.setText("Score");
                        throwButton.setEnabled(true);
                        scoreButton.setEnabled(true);
                        if (computerRollCount == 3) scoreDice();

                    }
                };
                if (computerRollCount > 0) {
                    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                    scheduler.schedule(rollDice, diceHoldDelay, delayUnit);
                } else {
                    rollDice.run();
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
    private void scoreDice() {
        userScore += userDice.scoreLatestResult();
        computerScore += computerDice.scoreLatestResult();
        userScoreLabel.setText(String.format("%d", userScore));
        computerScoreLabel.setText(String.format("%d", computerScore));
        throwButton.setText("Throw");
        scoreButton.setEnabled(false);
        setImageLabelsEnabled(false);
        computerDice.resetSelections();
        userRollCount = 0;
        computerRollCount = 0;
    }

    public JPanel getView() {
        return view;
    }
}