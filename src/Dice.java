import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kylejm on 31/12/14.
 */
public class Dice {
    private ArrayList<Die> faces;
    private static ImageIcon blankImage;

    public Dice() {
        faces = new ArrayList<Die>();
        for (int i = 0; i < 6; i++) {
            faces.add(new Die(i + 1));
        }
    }

    public ArrayList<Die> roll() {
        ArrayList<Die> results = new ArrayList<Die>();
        Random numGen = new Random();
        for (int i = 0; i < 5; i++) {
            int value = numGen.nextInt(7 - 1) + 1;
            results.add(faces.get(value - 1));
        }
        return results;
    }

    public ImageIcon getBlankFace() {
        if (blankImage == null) blankImage = new ImageIcon("Assets/blank.png");
        return blankImage;
    }
}
