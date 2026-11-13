package finance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FunctionalInterface
public interface LineSource {

    List<String> readLines() throws IOException;

    static LineSource fromUrl(String url) {
        return () -> {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    URI.create(url).toURL().openStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }
            return lines;
        };
    }

    static LineSource fromFile(String path) {
        return () -> Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
    }

    static LineSource fromLines(String... lines) {
        return () -> new ArrayList<>(Arrays.asList(lines));
    }

    static LineSource of(String pathOrUrl) {
        String lower = pathOrUrl.toLowerCase();
        if (lower.startsWith("http://") || lower.startsWith("https://")) {
            return fromUrl(pathOrUrl);
        }
        return fromFile(pathOrUrl);
    }
}
