package troll;

import clutter.Bag;
import clutter.SetBag;

import java.util.Collection;

public abstract class Op {
    private Bag<String, Op> inputsBySlot;
    private String name;
    private float[] buffer;

    protected Op(String name) {
        this.name = name;
        this.inputsBySlot = new SetBag<String, Op>();
        buffer = new float[0];
    }

    public abstract float[] execute(Configuration configuration, long currentSample);

    public final Op plug(String slot, Op op) {
        inputsBySlot.put(slot, op);
        return this;
    }

    public final void unplug(String slot, Op op) {
        inputsBySlot.remove(slot);
    }

    protected final float[] executeSlot(Configuration configuration, String slotName, long currentSample) {
        if (buffer.length != configuration.getSampleSize())
            buffer = new float[configuration.getSampleSize()];

        for (int i = 0; i < buffer.length; i++)
            buffer[i] = 0;

        Collection<Op> ops = inputsBySlot.getValues(slotName);

        for (Op op : ops) {
            float[] result = op.execute(configuration, currentSample);
            for (int i = 0; i < result.length; i++) {
                buffer[i] += result[i];
            }
        }

        return buffer;
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

