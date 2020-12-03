package lu.uni.serval.diff;

import java.io.File;
import java.io.IOException;
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
}
