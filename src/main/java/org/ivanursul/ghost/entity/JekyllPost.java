package org.ivanursul.ghost.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JekyllPost {

    private String fileName;
    private String content;

}
