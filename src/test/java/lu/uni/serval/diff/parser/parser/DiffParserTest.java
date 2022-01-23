package lu.uni.serval.diff.parser.parser;

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

import lu.uni.serval.diff.parser.Helpers;
import lu.uni.serval.diff.parser.patch.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class DiffParserTest {
    @Test
    void parseWithDiff1() throws IOException, URISyntaxException, MalformedDiffException {
        final File file = Helpers.getResourceFile("diff-1.txt");
        final Patches patches = new DiffParser().parse(new FileInputStream(file));

        assertEquals(1, patches.size());
        final Patch patch = patches.getByIndex(0);

        assertEquals(Patch.ChangeType.RENAME, patch.getChangeType());
        assertEquals("builtin-http-fetch.c", patch.getOldFile());
        assertEquals("http-fetch.c", patch.getNewFile());
        assertEquals("f3e63d7", patch.getOldVersion());
        assertEquals("e8f44ba", patch.getNewVersion());
        assertEquals(95, patch.getSimilarity());

        assertEquals(2, patch.getHunks().size());
        final Hunk hunk = patch.getHunks().get(0);
        final Hunk hunk2 = patch.getHunks().get(1);

        assertEquals(3, hunk.getChanges().size());
        final Change change = hunk.getChanges().get(0);

        assertEquals(Change.Type.REMOVE, change.getType());
        assertEquals("int cmd_http_fetch(int argc, const char **argv, const char *prefix)", change.getContent());
        assertEquals(4, change.getPosition());

        assertEquals(new LinePair(1, 1), hunk.lineMapping.get(0));
        assertEquals(new LinePair(8, 9), hunk.lineMapping.get(hunk.lineMapping.size()-1));

        assertEquals(new LinePair(18, 19), hunk2.lineMapping.get(0));
        assertEquals(new LinePair(23, 26), hunk2.lineMapping.get(hunk2.lineMapping.size()-1));

    }

    @Test
    void parseWithDiff2() throws IOException, URISyntaxException, MalformedDiffException {
        final File file = Helpers.getResourceFile("diff-2.txt");
        final Patches patches = new DiffParser()
                .setOldPrefix("old/")
                .setNewPrefix("new/")
                .parse(new FileInputStream(file));

        assertEquals(4, patches.size());
        final Patch patch = patches.getByIndex(2);

        assertEquals("/dev/null", patch.getOldFile());
        assertEquals("src/main/java/com/example/demo/integration/Belly.java", patch.getNewFile());

        assertEquals(Patch.ChangeType.ADD, patch.getChangeType());
    }

    @Test
    void parseWithDiff3() throws IOException, URISyntaxException, MalformedDiffException {
        final File file = Helpers.getResourceFile("diff_csv_1ae3639_911433.txt");
        final Patches patches = new DiffParser().parse(new FileInputStream(file));

        assertEquals(1, patches.size());
        final Patch patch = patches.getByIndex(0);

        final Hunk hunk = patch.getHunks().get(0);
        final Hunk hunk2 = patch.getHunks().get(1);

        assertEquals(8, hunk.lineMapping.size());
        assertEquals(19, hunk2.lineMapping.size());

        assertEquals(new LinePair(34, 34), hunk.lineMapping.get(0));
        assertEquals(new LinePair(39, 41), hunk.lineMapping.get(hunk.lineMapping.size()-1));

        assertEquals(new LinePair(50, 52), hunk2.lineMapping.get(0));
        assertEquals(new LinePair(64, 68), hunk2.lineMapping.get(hunk2.lineMapping.size()-1));

    }
}
