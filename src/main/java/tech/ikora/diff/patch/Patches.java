package tech.ikora.diff.patch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Patches implements Iterable<Patch> {
    private enum Side{
        OLD,
        NEW
    }

    private final List<Patch> patches = new ArrayList<>();

    @Override
    public Iterator<Patch> iterator() {
        return patches.iterator();
    }

    public int size() {
        return patches.size();
    }

    public void add(Patch patch){
        patches.add(patch);
    }

    public Patch getByIndex(int index){
        return patches.get(index);
    }

    public Optional<Patch> getByOldFile(String name, boolean strict){
        return forFile(name, strict, Side.OLD);
    }

    public Optional<Patch> getByNewFile(String name, boolean strict){
        return forFile(name, strict, Side.NEW);
    }

    private Optional<Patch> forFile(String name, boolean strict, Side side){
        return patches.stream().filter(p -> isFile(p, name, strict, side)).findFirst();
    }

    private static boolean isFile(Patch patch, String name, boolean strict, Side side){
        final String target = side == Side.OLD ? patch.getOldFile() : patch.getNewFile();
        return strict ? name.equals(target) : name.endsWith(target);
    }
}
