package troll;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

class Dac extends Op {
    public static final String INPUT = "input";
    private final byte[] output;
    private final SourceDataLine sdl;

    double ratio = (double)Short.MAX_VALUE / (double)Float.MAX_VALUE;

    public Dac() throws LineUnavailableException {
        super("DAC");

        AudioFormat af = new AudioFormat((float) 44100, 16, 1, true, false);
        //SourceDataLine sdl = AudioSystem.getSourceDataLine( af );
        sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        output = new byte[256];
    }


    @Override
    public float[] execute(Configuration config, long currentSample) {
        int sampleSize = config.getSampleSize();
        float[] input = executeSlot(config, INPUT, currentSample);

        for (int j = 0; j < sampleSize; j++) {
            short value = (short)(input[j] * ratio);
            output[(j * 2) + 1] = (byte) (value >> 8);
            output[(j * 2)] = (byte) value;
        }

        sdl.write(output, 0, output.length);
        return input;
    }

    @Override
    protected void finalize() throws Throwable {
        sdl.drain();
        sdl.stop();
        sdl.close();
    }

}
