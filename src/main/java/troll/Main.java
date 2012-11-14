package troll;

public class Main {

    public static final int SAMPLE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        long currentSample = 0;
        Configuration configuration = new Configuration(128, 44100);
        Oscillator oscillator = new Oscillator("OSC1");
        Dac dac = new Dac();
        dac.plug(Dac.INPUT, oscillator);

        for (int i = 0; i < SAMPLE_COUNT; i++) {
            dac.execute(configuration, currentSample);
            currentSample += configuration.getSampleSize();
        }
    }
}

