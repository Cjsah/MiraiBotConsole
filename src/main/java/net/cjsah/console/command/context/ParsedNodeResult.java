package net.cjsah.console.command.context;

public class ParsedNodeResult<T> {
    private final int start;
    private final int end;
    private final T result;

    public ParsedNodeResult(final int start, final int end, final T result) {
        this.start = start;
        this.end = end;
        this.result = result;
    }

    public IntRange getRange() {
        return new IntRange(this.start, this.end);
    }

    public T getResult() {
        return result;
    }
}
