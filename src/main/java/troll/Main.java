package troll;

public class Main {

    public static final int SAMPLE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        long currentSample = 0;
        Configuration configuration = new Configuration(512, 44100);

        Constant first = new Constant("first", 55);
        Constant second = new Constant("second", 110);
        Constant third = new Constant("third", 220);
        Constant fourth = new Constant("fourth", 440);

        Operator osc1 = new Oscillator("OSC1").connect(first, Constant.OUTPUT, Oscillator.FREQUENCY);
        Operator osc2 = new Oscillator("OSC2").connect(second, Constant.OUTPUT, Oscillator.FREQUENCY);
        Operator osc3 = new Oscillator("OSC3").connect(third, Constant.OUTPUT, Oscillator.FREQUENCY);
        Operator osc4 = new Oscillator("OSC4").connect(fourth, Constant.OUTPUT, Oscillator.FREQUENCY);

        Operator dac = new Dac()
                .connect(osc1, Oscillator.OUTPUT, Dac.INPUT)
                .connect(osc2, Oscillator.OUTPUT, Dac.INPUT)
                .connect(osc3, Oscillator.OUTPUT, Dac.INPUT)
                .connect(osc4, Oscillator.OUTPUT, Dac.INPUT);

        for (int i = 0; i < SAMPLE_COUNT; i++) {
            dac.execute(configuration, currentSample);
            currentSample += configuration.getSampleSize();
        }
    }
}

