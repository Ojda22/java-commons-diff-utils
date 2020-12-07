package lu.uni.serval.diff.parser.utils;

import lu.uni.serval.diff.parser.Helpers;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class StackTraceMapperTest {
    @Test
    void testMapStackTraceWithDiff() throws IOException, URISyntaxException {
        final String newStackTrace = Helpers.readStringFromResourcesFile("stacktrace-1.txt");
        final String diff = Helpers.readStringFromResourcesFile("diff-3.txt");

        final String oldStackTrace = StackTraceMapper.map(newStackTrace, diff);

        assertTrue(oldStackTrace.contains("com.example.demo.integration.StudentStepsDefinitions:verify_redurect_to_student__listpage:49"));
    }

    @Test
    void testMapStackTraceWithNoDiff() throws IOException, URISyntaxException {
        final String newStackTrace = Helpers.readStringFromResourcesFile("stacktrace-1.txt");
        final String diff = "";

        final String oldStackTrace = StackTraceMapper.map(newStackTrace, diff);

        assertEquals(newStackTrace, oldStackTrace);
    }
}