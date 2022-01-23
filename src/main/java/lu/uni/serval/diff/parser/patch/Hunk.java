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
import java.util.List;

public class Hunk {
    private final List<Change> changes = new ArrayList<>();

    /**
     * The lines numbers mapping
     */
    public List<LinePair> lineMapping = new ArrayList<>();

    /**
     * Add mapping between lines numbers from previous to current code version
     * @param oldVersionLine if the line does not exist in old version we assign zero
     * @param newVersionLine if the line does not exist in new version we assign zero
     *
     * Mapping example:
     *                       1 -> 1,
     *                       2 -> 2,
     *                    +  0 -> 3, (line 3 added in new version)
     *                       3 -> 4,
     *                    -  4 -> 0, (line 4 removed in old version)
     *                       5 -> 5
     */
    public void addLinePair(Integer oldVersionLine, Integer newVersionLine){
        lineMapping.add(new LinePair(oldVersionLine, newVersionLine));
    }

    public List<Change> getChanges() {
        return changes;
    }

    public void addChange(Change change){
        changes.add(change);
    }

}
