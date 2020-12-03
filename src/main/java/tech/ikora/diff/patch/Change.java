package lu.uni.serval.diff.patch;

public class Change {
    public enum Type{
        ADD,
        REMOVE
    }

    private final Type type;
    private final int position;
    private final String content;

    public Change(Type type, int position, String content) {
        this.type = type;
        this.position = position;
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

    public String getContent() {
        return content;
    }
}
