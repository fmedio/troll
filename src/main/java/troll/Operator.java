package troll;

import java.util.Map;
import java.util.TreeMap;

// TODO: multiple inputs summed for each slot
abstract class Op {
    private Map<String, Op> inputsBySlot;
    private String name;
    private float[] emptyBuffer;

    protected Op(String name) {
        this.name = name;
        this.inputsBySlot = new TreeMap<String, Op>();
        emptyBuffer = new float[0];
    }

    public abstract float[] execute(Configuration configuration, long currentSample);

    public final void plug(String slot, Op op) {
        inputsBySlot.put(slot, op);
    }

    public final void unplug(String slot, Op op) {
        inputsBySlot.remove(slot);
    }

    protected final float[] executeSlot(Configuration configuration, String slotName, long currentSample) {
        Op op = inputsBySlot.get(slotName);
        if (op == null) {
            if (emptyBuffer.length != configuration.getSampleSize())
                emptyBuffer = new float[configuration.getSampleSize()];

            return emptyBuffer;
        }

        return op.execute(configuration, currentSample);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Op op = (Op) o;

        if (name != null ? !name.equals(op.name) : op.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}

