package troll;

public class Oscillator extends Operator {
    private float[] buffer = new float[128];

    public static String FREQUENCY = "frequency";
    public static String OUTPUT = "output";

    public Oscillator(String name) {
        super(name);
    }

    @Override
    public void execute(Configuration configuration, long currentSample) {
        int sampleSize = configuration.getSampleSize();
        int samplingRate = configuration.getSamplingRate();

        float[] frequency = readInput(configuration, currentSample, FREQUENCY);

        if (buffer.length != sampleSize) {
            buffer = new float[sampleSize];
        }

        for (int i = 0; i < sampleSize; i++) {
            double angle = (double)((long)i + currentSample)/ ((double) samplingRate / (double)frequency[i]) * 2d * Math.PI;
            float value = (float) (Math.sin(angle) * Float.MAX_VALUE) / 4;
            buffer[i] = value;
        }

        publish(OUTPUT, buffer);
    }

}
