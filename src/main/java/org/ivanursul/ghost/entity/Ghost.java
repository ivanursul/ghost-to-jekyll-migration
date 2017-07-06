package org.ivanursul.ghost.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@EqualsAndHashCode
public class Ghost {

    private List<Db> db;


    public DbData dbData() {
        return db.get(0).getData();
    }

}
