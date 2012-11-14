package troll;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class Main {

    public static final int SAMPLE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        AudioFormat af = new AudioFormat((float) 44100, 16, 1, true, false);
        //SourceDataLine sdl = AudioSystem.getSourceDataLine( af );
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        byte[] output = new byte[256];
        long currentSample = 0;
        Configuration configuration = new Configuration(128, 44100);
        Oscillator oscillator = new Oscillator();

        double ratio = (double)Short.MAX_VALUE / (double)Float.MAX_VALUE;
        for (int i = 0; i < SAMPLE_COUNT; i++) {
            float[] sample = oscillator.nextSample(configuration, currentSample);
            currentSample += configuration.getSampleSize();

            for (int j = 0; j < sample.length; j++) {
                short value = (short)(sample[j] * ratio);
                output[(j * 2) + 1] = (byte) (value >> 8);
                output[(j * 2)] = (byte) value;
            }

            sdl.write(output, 0, output.length);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }

}

class Configuration {
    private int sampleSize;
    private int samplingRate;

    Configuration(int sampleSize, int samplingRate) {
        this.sampleSize = sampleSize;
        this.samplingRate = samplingRate;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public int getSamplingRate() {
        return samplingRate;
    }
}

class Oscillator {
    private float[] sample = new float[128];

    public float[] nextSample(Configuration config, long currentSample) {
        int sampleSize = config.getSampleSize();
        int samplingRate = config.getSamplingRate();

        if (sample.length != sampleSize) {
            sample = new float[sampleSize];
        }

        for (int i = 0; i < sampleSize; i++) {
            double angle = (double)((long)i + currentSample)/ ((double) samplingRate / 220d) * 2d * Math.PI;
            float value = (float) (Math.sin(angle) * Float.MAX_VALUE);
            sample[i] = value;
        }
        return sample;
    }
}
