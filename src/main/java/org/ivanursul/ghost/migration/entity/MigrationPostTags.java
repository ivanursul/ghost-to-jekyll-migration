package org.ivanursul.ghost.migration.entity;

import org.ivanursul.ghost.entity.DbData;
import org.ivanursul.ghost.entity.PostTag;
import org.ivanursul.ghost.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class MigrationPostTags {

    private DbData data;

    public Map<Integer, List<Tag>> postTags() {
        Map<Integer, Tag> tags = data.getTags().stream()
                .collect(Collectors.toMap(Tag::getId, Function.identity()));

        return data.getPosts_tags().stream()
                .collect(Collectors.groupingBy(
                        PostTag::getPost_id
                )).entrySet().stream()
                .map(entry -> new MigrationPostTag(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(tag -> new Tag(
                                                tag.getTag_id(),
                                                tags.get(tag.getTag_id()).getName(),
                                                tags.get(tag.getTag_id()).getSlug()
                                        )
                                )
                                .filter(tag -> tag.getName() != null)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toMap(MigrationPostTag::getPostId, MigrationPostTag::getTags));
    }

}
