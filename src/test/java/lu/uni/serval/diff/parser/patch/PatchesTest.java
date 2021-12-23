package lu.uni.serval.diff.parser.patch;

import lu.uni.serval.diff.parser.parser.DiffParser;
import lu.uni.serval.diff.parser.parser.MalformedDiffException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import lu.uni.serval.diff.parser.Helpers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PatchesTest {
    private static Patches patches1;
    private static Patches patches2;

    @BeforeAll
    static void parse() throws IOException, URISyntaxException, MalformedDiffException {
        final File file1 = Helpers.getResourceFile("diff-1.txt");
        patches1 = new DiffParser().parse(new FileInputStream(file1));

        final File file2 = Helpers.getResourceFile("diff-2.txt");
        patches2 = new DiffParser()
                .setOldPrefix("old/")
                .setNewPrefix("new/")
                .parse(new FileInputStream(file2));
    }

    @Test
    void testGetByIndex(){
        Patch patch0 = patches2.getByIndex(0);
        assertEquals("pom.xml", patch0.getOldFile());
        assertEquals("pom.xml", patch0.getNewFile());

        Patch patch3 = patches2.getByIndex(3);
        assertEquals("src/main/java/com/example/demo/model/Student.java", patch3.getOldFile());
        assertEquals("src/main/java/com/example/demo/model/Student.java", patch3.getNewFile());
    }

    @ParameterizedTest
    @CsvSource({
            "builtin-http-fetch.c,true,builtin-http-fetch.c",
            "builtin-http-fetch.c,false,builtin-http-fetch.c",
            "some/path/builtin-http-fetch.c,false,builtin-http-fetch.c"
    })
    void testGetByOldFile(String path, boolean strict, String expected){
        final Optional<Patch> patch = patches1.getByOldFile(path, strict);

        assertTrue(patch.isPresent());
        assertEquals(expected, patch.get().getOldFile());
    }

    @Test
    void testGetByOldFileStrictFailing(){
        final String path = "some/path/builtin-http-fetch.c";
        final Optional<Patch> patch = patches1.getByOldFile(path, true);

        assertFalse(patch.isPresent());
    }

    @Test
    void testGetByOldFileWithWrongPath(){
        final String path = "http-fetch-fake.c";
        final Optional<Patch> patch = patches1.getByOldFile(path, false);

        assertFalse(patch.isPresent());
    }

    @ParameterizedTest
    @CsvSource({
            "http-fetch.c,true,http-fetch.c",
            "http-fetch.c,false,http-fetch.c",
            "some/path/http-fetch.c,false,http-fetch.c"
    })
    void testGetByNewFile(String path, boolean strict, String expected){
        final Optional<Patch> patch = patches1.getByNewFile(path, strict);

        assertTrue(patch.isPresent());
        assertEquals(expected, patch.get().getNewFile());
    }

    @Test
    void testGetByNewFileStrictFailing(){
        final String path = "some/path/http-fetch.c";
        final Optional<Patch> patch = patches1.getByNewFile(path, true);

        assertFalse(patch.isPresent());
    }

    @Test
    void testGetByNewFileWithWrongPath(){
        final String path = "http-fetch-fake.c";
        final Optional<Patch> patch = patches1.getByNewFile(path, false);

        assertFalse(patch.isPresent());
    }
}