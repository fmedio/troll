package troll;

import junit.framework.TestCase;

public class AdsrTest extends TestCase {

    public static final int SAMPLING_RATE = 1000;

    public void testBuffer() {
        Constant constant = new Constant("foo", Float.MAX_VALUE);
        Adsr adsr = new Adsr("poop");
        adsr.setAttackMs(1000);
        adsr.setDecayMs(1000);
        adsr.setSustain(Float.MAX_VALUE / 2f);
        adsr.setReleaseMs(1000);
        adsr.connect(constant, Constant.OUTPUT, Adsr.GATE);
        Configuration configuration = new Configuration(500, 1000);
        adsr.execute(configuration, 0);
        float[] firstBuffer = adsr.read(configuration, Adsr.OUTPUT);
        assertEquals(0f, firstBuffer[0]);
        adsr.execute(configuration, 500);
        float[] secondBuffer = adsr.read(configuration, Adsr.OUTPUT);
        assertEquals(Float.MAX_VALUE / 2f, secondBuffer[0]);
        System.out.println(firstBuffer.length);
    }

    public void testValues() {
        Adsr adsr = new Adsr("poop");
        adsr.setAttackMs(1000);
        adsr.setDecayMs(1000);
        adsr.setSustain(Float.MAX_VALUE / 2f);
        adsr.setReleaseMs(1000);

        // Begin attack
        assertEquals(0f, adsr.currentValue(SAMPLING_RATE, 0, 0, -1));
        assertEquals((float) Float.MAX_VALUE/2f, (float) adsr.currentValue(SAMPLING_RATE, 500, 0, -1));
        assertEquals(Float.MAX_VALUE, adsr.currentValue(SAMPLING_RATE, 1000, 0, -1));

        // Begin decay
        assertEquals((float) (3d / 4d * Float.MAX_VALUE), adsr.currentValue(SAMPLING_RATE, 1500, 0, -1));
        assertEquals((float) (Float.MAX_VALUE / 2d), adsr.currentValue(SAMPLING_RATE, 2000, 0, -1));

        // Sustain
        assertEquals((float) (Float.MAX_VALUE / 2d), adsr.currentValue(SAMPLING_RATE, 2500, 0, -1));

        // Begin release
        assertEquals((float)(Float.MAX_VALUE / 2d), adsr.currentValue(SAMPLING_RATE, 2500, 0, 2500));
        assertEquals((float)(1d/4d * Float.MAX_VALUE), adsr.currentValue(SAMPLING_RATE, 3000, 0, 2500));
        assertEquals(0f, adsr.currentValue(SAMPLING_RATE, 3500, 0, 2500));
    }
}
