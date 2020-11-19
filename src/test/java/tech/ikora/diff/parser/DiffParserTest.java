package tech.ikora.diff.parser;

import org.junit.jupiter.api.Test;
import tech.ikora.diff.Helpers;
import tech.ikora.diff.patch.Change;
import tech.ikora.diff.patch.Hunk;
import tech.ikora.diff.patch.Patch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiffParserTest {
    @Test
    void parseWithDiff1() throws IOException, URISyntaxException, MalformedDiffException {
        final File file = Helpers.getResourceFile("diff-1.txt");
        final List<Patch> patches = new DiffParser().parse(new FileInputStream(file));

        assertEquals(1, patches.size());
        final Patch patch = patches.get(0);

        assertEquals(Patch.ChangeType.RENAME, patch.getChangeType());
        assertEquals("builtin-http-fetch.c", patch.getOldFile());
        assertEquals("http-fetch.c", patch.getNewFile());
        assertEquals("f3e63d7", patch.getOldVersion());
        assertEquals("e8f44ba", patch.getNewVersion());
        assertEquals(95, patch.getSimilarity());

        assertEquals(2, patch.getHunks().size());
        final Hunk hunk = patch.getHunks().get(0);

        assertEquals(3, hunk.getChanges().size());
        final Change change = hunk.getChanges().get(0);

        assertEquals(Change.Type.REMOVE, change.getType());
        assertEquals("int cmd_http_fetch(int argc, const char **argv, const char *prefix)", change.getContent());
        assertEquals(4, change.getPosition());
    }

    @Test
    void parseWithDiff2() throws IOException, URISyntaxException, MalformedDiffException {
        final File file = Helpers.getResourceFile("diff-2.txt");
        final List<Patch> patches = new DiffParser()
                .setOldPrefix("old/")
                .setNewPrefix("new/")
                .parse(new FileInputStream(file));

        assertEquals(4, patches.size());
        final Patch patch = patches.get(2);

        assertEquals("/dev/null", patch.getOldFile());
        assertEquals("src/main/java/com/example/demo/integration/Belly.java", patch.getNewFile());

        assertEquals(Patch.ChangeType.ADD, patch.getChangeType());
    }
}