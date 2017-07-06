package org.ivanursul.ghost.entity;

import lombok.Data;

@Data
public class PostTag {

    private int id;
    private int post_id;
    private int tag_id;

}
