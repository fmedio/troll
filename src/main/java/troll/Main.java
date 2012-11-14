package troll;

public class Main {

    public static final int SAMPLE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        long currentSample = 0;
        Configuration configuration = new Configuration(128, 44100);

        Op osc1 = new Oscillator("OSC1").plug(Oscillator.FREQUENCY, new Constant("440", 880));
        Op osc2 = new Oscillator("OSC2").plug(Oscillator.FREQUENCY, new Constant("220", 250));
        Op osc3 = new Oscillator("OSC3").plug(Oscillator.FREQUENCY, new Constant("110", 110));

        Op dac = new Dac()
                .plug(Dac.INPUT, osc1)
                .plug(Dac.INPUT, osc2)
                .plug(Dac.INPUT, osc3);

        for (int i = 0; i < SAMPLE_COUNT; i++) {
            dac.execute(configuration, currentSample);
            currentSample += configuration.getSampleSize();
        }
    }
}

