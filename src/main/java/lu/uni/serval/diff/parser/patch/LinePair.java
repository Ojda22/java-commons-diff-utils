package lu.uni.serval.diff.parser.patch;

import java.util.Objects;

public class LinePair {

    private final Integer oldVersionLine;
    private final Integer newVersionLine;

    public LinePair(Integer oldVersionLine, Integer newVersionLine) {
        assert oldVersionLine != null;
        assert newVersionLine != null;
        this.oldVersionLine = oldVersionLine;
        this.newVersionLine = newVersionLine;
    }

    public Integer getOldVersionLine() {
        return oldVersionLine;
    }

    public Integer getNewVersionLine() {
        return newVersionLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinePair linesPair = (LinePair) o;
        return Objects.equals(oldVersionLine, linesPair.oldVersionLine) && Objects.equals(newVersionLine, linesPair.newVersionLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldVersionLine, newVersionLine);
    }
}
