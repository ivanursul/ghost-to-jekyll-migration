package org.ivanursul.ghost.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Post {

    private int id;
    private String slug;
    private String title;
    private Date published_at;
    private String markdown;


    public boolean isPublished() {
        return published_at != null;
    }
}
