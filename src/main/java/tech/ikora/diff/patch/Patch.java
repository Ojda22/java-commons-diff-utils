package lu.uni.serval.diff.patch;

import java.util.ArrayList;
import java.util.List;

public class Patch {
    private static final String NO_FILE = "/dev/null";

    public enum ChangeType {
        ADD,
        MODIFY,
        DELETE,
        RENAME,
        COPY
    }

    private String oldFile = NO_FILE;
    private String newFile = NO_FILE;

    private String oldVersion = "";
    private String newVersion = "";

    private int similarity = -1;

    private final List<Hunk> hunks = new ArrayList<>();

    private ChangeType changeType = ChangeType.MODIFY;

    public String getOldFile() {
        return oldFile;
    }

    public void setOldFile(String oldFile) {
        this.oldFile = oldFile;
    }

    public String getNewFile() {
        return newFile;
    }

    public void setNewFile(String newFile) {
        this.newFile = newFile;
    }

    public String getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public List<Hunk> getHunks() {
        return hunks;
    }

    public void addHunk(Hunk hunk){
        this.hunks.add(hunk);
    }
}
