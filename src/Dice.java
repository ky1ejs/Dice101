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

    public Dice() {
        for (int i = 0; i < 6; i++) {
            faces.add(new Die(i + 1));
        }
    }

    public ArrayList<Die> roll() {
        latestResult = new ArrayList<Die>();
        Random numGen = new Random();
        for (int i = 0; i < 5; i++) {
            int value = numGen.nextInt(7 - 1) + 1;
            latestResult.add(faces.get(value - 1));
        }
        return latestResult;
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

    public ArrayList<Die> getLatestResult() {
        return latestResult;
    }
}
