package org.ivanursul.ghost.migration.entity;

import javaslang.control.Try;
import lombok.Data;
import org.ivanursul.ghost.GhostRegex;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Data
public class GhostImage {

    private final String image;
    private final String url;
    private final String imageName;

    public GhostImage(String image) {
        this(
                image,
                new GhostRegex("(!\\[\\]\\()(.*)(?=\\))", image)
                        .findFirst(2)
                        .map(u -> "https://ivanursul.com" + u)
                        .get(),
                image
                        .substring(image.lastIndexOf("/") + 1, image.length() - 1)
                        .replace("?style=centerme", "")
        );
    }

    public GhostImage(String image, String url, String imageName) {
        this.image = image;
        this.url = url;
        this.imageName = imageName;
    }

    public void download() {
        InputStream in = Try.of(() -> new URL(url).openStream()).get();
        Try.of(() -> Files.copy(in, Paths.get("target/images/" + imageName))).get();
    }
}
