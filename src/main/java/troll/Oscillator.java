package troll;

public class Oscillator extends Op {
    private float[] buffer = new float[128];

    public Oscillator(String name) {
        super(name);
    }

    @Override
    public float[] execute(Configuration configuration, long currentSample) {
        int sampleSize = configuration.getSampleSize();
        int samplingRate = configuration.getSamplingRate();

        if (buffer.length != sampleSize) {
            buffer = new float[sampleSize];
        }

        for (int i = 0; i < sampleSize; i++) {
            double angle = (double)((long)i + currentSample)/ ((double) samplingRate / 440d) * 2d * Math.PI;
            float value = (float) (Math.sin(angle) * Float.MAX_VALUE);
            buffer[i] = value;
        }
        return buffer;
    }

}