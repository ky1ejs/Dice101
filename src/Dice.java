import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kylejm on 31/12/14.
 */
public class Dice {
    private ArrayList<Die> faces = new ArrayList<Die>();
    private static ImageIcon blankImage;
    private ArrayList<Die> latestResult = new ArrayList<Die>();
    private ArrayList<Integer> selectedDieFaceIndexes = new ArrayList<Integer>();
    private Random numGen = new Random();

    public Dice() {
        for (int i = 0; i < 6; i++) {
            faces.add(new Die(i + 1));
        }
    }

    public ArrayList<Die> roll() {
        if (latestResult.size() == 0) {
            latestResult = new ArrayList<Die>();
            for (int i = 0; i < 5; i++) {
                latestResult.add(newRandomDieFace());
            }
        } else {
            for (int i = 0; i < 5; i++) {
                if (!selectedDieFaceIndexes.contains(new Integer(i))) {
                    latestResult.set(i, newRandomDieFace());
                }
            }
        }
        return latestResult;
    }

    private Die newRandomDieFace() {
        return faces.get(numGen.nextInt(6));
    }

    public ImageIcon getBlankFace() {
        if (blankImage == null) blankImage = new ImageIcon("Assets/blank.png");
        return blankImage;
    }

    public int scoreLatestResult() {
        int score = 0;
        for (Die dieFace : latestResult) {
            score += dieFace.getValue();
        }
        return score;
    }

    public ImageIcon toggleSelectionOfDieAtIndex(int index) {
        ImageIcon image;
        if (selectedDieFaceIndexes.contains(new Integer(index))) {
            selectedDieFaceIndexes.remove(new Integer(index));
            image = latestResult.get(index).getDieImage();
        } else {
            selectedDieFaceIndexes.add(new Integer(index));
            image = latestResult.get(index).getDieSelectedImage();
        }
        return image;
    }

    public void resetSelections() {
        selectedDieFaceIndexes = new ArrayList<Integer>();
    }

    public ArrayList<Die> getLatestResult() {
        return latestResult;
    }
}
