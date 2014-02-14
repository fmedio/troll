package troll;

public class Configuration {
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
