package troll;

public class OneNoteKeyboard extends Operator {
    public static final String FREQUENCY = "frequency";
    public static final String GATE = "gate";

    private float[] freq;
    private float[] gate;


    protected OneNoteKeyboard(String name) {
        super(name);
        freq = new float[0];
        gate = new float[0];
    }

    @Override
    protected void execute(Configuration configuration, long currentSample) {
        if (freq.length != configuration.getSampleSize())
            freq = new float[configuration.getSampleSize()];

        if (gate.length != configuration.getSampleSize())
            gate = new float[configuration.getSampleSize()];

        for (int i = 0; i < freq.length; i++)
            freq[i] = 220f;

        for (int i = 0; i < gate.length; i++) {
            gate[i] = Float.MAX_VALUE;
        }

        publish(FREQUENCY, freq);
        publish(GATE, gate);
    }

}
