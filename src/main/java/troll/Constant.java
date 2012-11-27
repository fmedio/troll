package troll;

public class Constant extends Operator {
    private float value;
    private float[] buffer;

    public static final String OUTPUT = "output";

    public Constant(String name, float value) {
        super(name);
        this.value = value;
        buffer = new float[0];
    }

    @Override
    public void execute(Configuration configuration, long currentSample) {
        if (buffer.length != configuration.getSampleSize())
            buffer = new float[configuration.getSampleSize()];

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = value;
        }

        publish(OUTPUT, buffer);
    }


}

