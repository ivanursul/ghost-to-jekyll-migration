package org.ivanursul.ghost;

public class Main {

    public static void main(String[] args) throws Exception {
        new GhostMigration("blog.json").migrate();
    }

}
