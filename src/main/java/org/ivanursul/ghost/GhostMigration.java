package org.ivanursul.ghost;

import javaslang.control.Try;
import lombok.Data;
import org.ivanursul.ghost.entity.*;
import org.ivanursul.ghost.migration.entity.GhostImage;
import org.ivanursul.ghost.migration.entity.MigrationPostTags;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Data
public class GhostMigration {

    private final String ghostLocation;

    public void migrate() throws Exception {
        allowSelfAssignedSslRequests();
        createTargetFolders();

        Ghost ghost = new GhostReader(ghostLocation).read();
        DbData data = ghost.dbData();

        Map<Integer, List<Tag>> postTags = new MigrationPostTags(data).postTags();

        data.getPosts().parallelStream()
                .filter(post -> post.isPublished())
                .forEach(post -> {
                    JekyllPost jekyllPost = jekyllPost(post, postTags.get(post.getId()));
                    Try.of(() -> Files.write(
                            Paths.get(String.format("target/posts/%s", jekyllPost.getFileName())),
                            jekyllPost.getContent().getBytes()
                    ));

                    System.out.println("Finished '" + post.getTitle() +"'");
                });
    }

    private JekyllPost jekyllPost(Post post, List<Tag> tags) {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(post.getPublished_at());
        String prefixDate = new SimpleDateFormat("yyyy-MM-dd").format(post.getPublished_at());

        String name = prefixDate + "-" + post.getSlug() + ".markdown";
        String markdown = post.getMarkdown();

        List<GhostImage> images = processImages(markdown);
        images.stream().forEach(image -> image.download());

        markdown = replaceImageUrls(markdown, images);

        String content = new StringBuilder()
                .append("---\n")
                .append("layout: \"post\"\n")
                .append("title:  \"" + post.getTitle() + "\"\n")
                .append("date: " + date + "\n")
                .append("permalink: " + post.getSlug() + "\n")
                .append(
                        Optional.ofNullable(tags)
                                .map(t -> "tags: " + t.stream().map(tag -> tag.getName()).collect(joining(",")) + "\n")
                                .orElse("")
                )
                .append("---\n")
                .append("\n")
                .append("\n")
                .append(markdown)
                .toString();

        return new JekyllPost(name, content);
    }

    private String replaceImageUrls(String markdown, List<GhostImage> images) {
        return images.stream()
                .map(image -> image.getImage())
                .reduce(markdown, (str, image) -> str.replace(
                        image,
                        String.format(
                                "![](assets/images/%s)",
                                image.substring(image.lastIndexOf("/") + 1, image.length() - 1).replace("?style=centerme", "")
                        )
                ));
    }

    private List<GhostImage> processImages(String markdown) {
        return new GhostRegex("!\\[\\]\\(.*?\\)", markdown).list().parallelStream()
                .filter(image -> !image.contains("http://") && !image.contains("https://"))
                .map(image -> new GhostImage(image))
                .collect(toList());
    }

    private void createTargetFolders() {
        Try.of(() -> Files.createDirectory(Paths.get("target/images"))).get();
        Try.of(() -> Files.createDirectory(Paths.get("target/posts"))).get();
    }

    private void allowSelfAssignedSslRequests() throws Exception {
        SSLContext e = SSLContext.getInstance("TLS");
        e.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(e);
        HttpsURLConnection.setDefaultSSLSocketFactory(e.getSocketFactory());
    }
}
