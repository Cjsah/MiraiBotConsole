package net.cjsah.console.command.context;

public class IntRange {
    private final int start;
    private final int end;

    public IntRange(final int pos) {
        this(pos, pos);
    }

    public IntRange(final int start, final int end) {
        this.start = start;
        this.end = end;
    }

    public IntRange(final IntRange a, final IntRange b) {
        this(Math.min(a.start, b.start), Math.max(a.end, b.end));
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public boolean isEmpty() {
        return this.start == this.end;
    }

}
