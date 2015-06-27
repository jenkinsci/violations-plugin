package hudson.plugins.violations.render;

public class BlockData {

    private final int from;
    private final int to;
    private final String content;
    private final String file;

    public BlockData(int from, int to, String content, String file) {
        this.from = from;
        this.to = to;
        this.file = file;
        this.content = content;
    }

    public String getFile() {
        return file;
    }

    public String getContent() {
        return content;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }
}
