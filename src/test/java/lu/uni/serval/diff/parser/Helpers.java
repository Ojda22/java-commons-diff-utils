package lu.uni.serval.diff.parser;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class Helpers {
    public static File getResourceFile(String name) throws IOException, URISyntaxException {
        URL resource = Helpers.class.getClassLoader().getResource(name);
        if (resource == null) {
            throw new IOException("Failed to locate resource template for project analytics");
        }

        return Paths.get(resource.toURI()).toFile();
    }

    public static String readStringFromResourcesFile(String name) throws IOException, URISyntaxException {
        final File file = getResourceFile(name);

        try (InputStream inputStream = new FileInputStream(file)){
            return readFromInputStream(inputStream);
        }
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        final StringBuilder resultStringBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }

        return resultStringBuilder.toString();
    }
}
