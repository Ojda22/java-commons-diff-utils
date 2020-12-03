package lu.uni.serval.diff.patch;

import java.util.ArrayList;
import java.util.List;

public class Hunk {
    private final List<Change> changes = new ArrayList<>();

    public List<Change> getChanges() {
        return changes;
    }

    public void addChange(Change change){
        changes.add(change);
    }
}
