package org.ivanursul.ghost.entity;

import lombok.Data;

import java.util.List;

@Data
public class DbData {

    private List<Post> posts;
    private List<PostTag> posts_tags;
    private List<Tag> tags;

}
