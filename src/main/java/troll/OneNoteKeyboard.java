package troll;

import javax.swing.*;

public class OneNoteKeyboard extends Operator {
    public static final String FREQUENCY = "frequency";
    public static final String GATE = "gate";

    private float[] freq;
    private float[] gate;
    private final JButton button;


    protected OneNoteKeyboard(String name) {
        super(name);
        freq = new float[0];
        gate = new float[0];
        JFrame frame = new JFrame("foo");
        button = new JButton("bang");
        frame.getContentPane().add(button);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    @Override
    protected void execute(Configuration configuration, long currentSample) {
        if (freq.length != configuration.getSampleSize())
            freq = new float[configuration.getSampleSize()];

        if (gate.length != configuration.getSampleSize())
            gate = new float[configuration.getSampleSize()];

        for (int i = 0; i < freq.length; i++)
            freq[i] = 220f;

        if (button.getModel().isPressed()) {
            for (int i = 0; i < gate.length; i++) {
                gate[i] = Float.MAX_VALUE;
            }
        } else {
            for (int i = 0; i < gate.length; i++) {
                gate[i] = 0;
            }
        }

        publish(FREQUENCY, freq);
        publish(GATE, gate);
    }

}
