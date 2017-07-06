package org.ivanursul.ghost.migration.entity;

import org.ivanursul.ghost.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MigrationPostTag {

    private int postId;
    private List<Tag> tags;

}
