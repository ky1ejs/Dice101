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
    private JButton newGameButton;
    private int targetScore;
    private boolean rollOutMode;
    private int userWins;
    private int computerWins;
    private JLabel userWinsLabel = new JLabel("    ");
    private JLabel computerWinsLabel = new JLabel("    ");


    public DiceMatViewController(int targetScore, int initialUserWins, int initialComputerWins) {
        this.targetScore = targetScore;
        this.userWins = initialUserWins;
        this.computerWins = initialComputerWins;
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
        userStatsGrid.setBackground(new Color(96,176,249));
        computerStatsGrid.setBackground(new Color(200,104,205));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        userStatsGrid.add(new JLabel("User"), constraints);
        computerStatsGrid.add(new JLabel("Computer"), constraints);
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        userStatsGrid.add(new JLabel("Score:"), constraints);
        computerStatsGrid.add(new JLabel("Score:"), constraints);
        constraints.gridx = 1;
        userStatsGrid.add(userScoreLabel, constraints);
        computerStatsGrid.add(computerScoreLabel, constraints);
        constraints.gridx = 2;
        userStatsGrid.add(new JLabel("Rolls left:"), constraints);
        computerStatsGrid.add(new JLabel("Rolls left:"), constraints);
        constraints.gridx = 3;
        userStatsGrid.add(userRollCountLabel, constraints);
        computerStatsGrid.add(computerRollCountLabel, constraints);
        constraints.gridx = 4;
        userStatsGrid.add(new JLabel("Wins:"), constraints);
        computerStatsGrid.add(new JLabel("Wins:"), constraints);
        constraints.gridx = 5;
        userStatsGrid.add(userWinsLabel, constraints);
        computerStatsGrid.add(computerWinsLabel, constraints);
        userWinsLabel.setText(String.format("%d", userWins));
        computerWinsLabel.setText(String.format("%d", computerWins));
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
        } else if (e.getComponent() == newGameButton) {
            newGame();
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
                if (!rollOutMode) {
                    userDice.resetSelections();
                    userRollCount++;
                    userRollCountLabel.setText(String.format("%d", 3 - userRollCount));
                }
            }
        };
        if (!rollOutMode) throwButton.setText(String.format("You're holding %d dice", userDice.selectionCount()));
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
                        if (!rollOutMode) {
                            computerRollCount++;
                            computerRollCountLabel.setText(String.format("%d", 3 - computerRollCount));
                            throwButton.setText("Re-roll");
                            scoreButton.setText("Score");
                        }
                        throwButton.setEnabled(true);
                        scoreButton.setEnabled(true);
                        if (computerRollCount == 3 || rollOutMode) scoreDice();

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
        userRollCountLabel.setText(String.format("%d", userRollCount));
        computerRollCountLabel.setText(String.format("%d", computerRollCount));
        if (rollOutMode) {
            if (userScore == computerScore) {
                JOptionPane.showMessageDialog(null, "It's a tie again! Keep rolling!", "Keep going!", JOptionPane.PLAIN_MESSAGE);
            } else {
                rollOutMode = false;
                if (userScore > computerScore) {
                    userWon("Congratulations!", "You won the tie breaker!", "You won!");
                } else {
                    computerWon("Unlucky", "The computer won the tie breaker", "The computer won");
                }
            }
        } else if (userScore >= targetScore || computerScore >= targetScore) {
            if (userScore == computerScore) {
                String alertMessage = "It's a tie! You now have a roll out. The first person to roll the highest score wins. There will be no re-rolls";
                JOptionPane.showMessageDialog(null, alertMessage, "Tie breaker!", JOptionPane.PLAIN_MESSAGE);
                scoreButton.setText("TIE BREAKER!");
                userScore = 0;
                computerScore = 0;
                userScoreLabel.setText(String.format("%d", userScore));
                computerScoreLabel.setText(String.format("%d", computerScore));
                rollOutMode = true;
                throwButton.setEnabled(true);
            } else {
                if (userScore >= targetScore && computerScore >= targetScore) {
                    if (userScore > computerScore) {
                        userWon("That was close", String.format("You won!\n\nYou both got to %d, but your score was higher", targetScore), "You won!");
                    } else if (computerScore > userScore) {
                        computerWon("Bad luck", String.format("The computer won\n\nYou both got to %d, but the computers score was higher", targetScore), "The computer won");
                    }
                } else if (userScore >= targetScore)  {
                    userWon("Congratulations!", "You won!", "You won!");
                }
                else {
                    computerWon("BOOOO!", "The computer won", "The computer won");
                }
            }
        }
    }

    private void userWon(String winTitle, String winMessage, String newGameButtonText) {
        userWins++;
        userWinsLabel.setText(String.format("%d", userWins));
        newWinnerSet(winTitle, winMessage, newGameButtonText);
    }

    private void computerWon(String winTitle, String winMessage, String newGameButtonText) {
        computerWins++;
        computerWinsLabel.setText(String.format("%d", computerWins));
        newWinnerSet(winTitle, winMessage, newGameButtonText);
    }

    private void newWinnerSet(String winTitle, String winMessage, String newGameButtonText) {
        newGameButtonText += " - click here to play a new game";
        initNewGameButton();
        newGameButton.setText(newGameButtonText);
        JOptionPane.showMessageDialog(null, winMessage, winTitle, JOptionPane.PLAIN_MESSAGE);
    }

    private void initNewGameButton() {
        if (newGameButton == null) {
            newGameButton = new JButton();
            newGameButton.addMouseListener(this);
        }
        view.remove(throwButton);
        view.remove(scoreButton);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        constraints.gridy = 2;
        view.add(newGameButton, constraints);
    }

    private void newGame() {
        view.remove(newGameButton);
        initButtons();
        throwButton.setEnabled(true);
        //the re-validate and repaint methods below need to be called to force the newGame button to be
        //removed from screen and the other buttons to be re-drawn
        throwButton.revalidate();
        throwButton.repaint();
        scoreButton.revalidate();
        scoreButton.repaint();
        userScore = 0;
        computerScore = 0;
        userScoreLabel.setText(String.format("%d", userScore));
        computerScoreLabel.setText(String.format("%d", computerScore));
    }

    public JPanel getView() {
        return view;
    }

    public int getUserWins() {
        return userWins;
    }

    public int getComputerWins() {
        return  computerWins;
    }
}