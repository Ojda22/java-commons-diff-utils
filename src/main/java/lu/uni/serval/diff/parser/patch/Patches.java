package lu.uni.serval.diff.parser.patch;

/*-
 * #%L
 * Commons Diff Utils
 * %%
 * Copyright (C) 2019 - 2021 University of Luxembourg
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
        return strict ? name.equals(target) : (target.endsWith(name) || name.endsWith(target));
    }
}
