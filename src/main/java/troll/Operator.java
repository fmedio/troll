package troll;

import clutter.Bag;
import clutter.SetBag;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public abstract class Operator {
    private Map<String, SlotValue> values;
    private Bag<String, Connection> inputs;

    private String name;
    private float[] buffer;
    private long watermark;

    protected Operator(String name) {
        this.name = name;
        values = new TreeMap<String, SlotValue>();
        inputs = new SetBag<String, Connection>();
        buffer = new float[0];
    }

    public final void refresh(Configuration configuration, long currentSample) {
        if (watermark != currentSample) {
            execute(configuration, currentSample);
            watermark = currentSample;
        }
    }

    protected abstract void execute(Configuration configuration, long currentSample);

    protected final void publish(String slotName, float[] value) {
        SlotValue slotValue = values.get(slotName);
        if (slotValue == null) {
            slotValue = new SlotValue();
            slotValue.name = slotName;
            values.put(slotName, slotValue);
        }

        slotValue.data = value;
    }

    protected final float[] readInput(Configuration configuration, long currentSample, String slotName) {
        normalizeBuffer(configuration);
        Collection<Connection> inputs = this.inputs.getValues(slotName);
        if (inputs.size() == 0)
            return buffer;

        for (Connection connection : inputs) {
            connection.source.refresh(configuration, currentSample);
            float[] result = connection.source.read(configuration, connection.remoteSlot);
            sum(result);
        }

        return buffer;
    }

    private float[] read(Configuration configuration, String slotName) {
        normalizeBuffer(configuration);

        SlotValue value = values.get(slotName);
        if (value == null)
            return buffer;

        return value.data;
    }

    private void sum(float[] buf) {
        for (int i = 0; i < buf.length; i++) {
            buffer[i] += buf[i];
        }
    }
    private void normalizeBuffer(Configuration configuration) {
        if (buffer.length != configuration.getSampleSize())
            buffer = new float[configuration.getSampleSize()];

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 0;
        }
    }

    public Operator connect(Operator input, String remoteSlot, String localSlot) {
        inputs.put(localSlot, new Connection(input, remoteSlot));
        return this;
    }

    private class SlotValue {
        String name;
        float[] data;
    }

    private class Connection {
        Operator source;
        String remoteSlot;

        private Connection(Operator source, String remoteSlot) {
            this.source = source;
            this.remoteSlot = remoteSlot;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Connection that = (Connection) o;

            if (remoteSlot != null ? !remoteSlot.equals(that.remoteSlot) : that.remoteSlot != null) return false;
            if (source != null ? !source.equals(that.source) : that.source != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = source != null ? source.hashCode() : 0;
            result = 31 * result + (remoteSlot != null ? remoteSlot.hashCode() : 0);
            return result;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Operator operator = (Operator) o;

        if (name != null ? !name.equals(operator.name) : operator.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
