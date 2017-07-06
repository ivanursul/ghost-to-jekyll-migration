package org.ivanursul.ghost;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class GhostRegex {

    private final String pattern;
    private final String string;

    public List<String> list() {
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile(pattern)
                .matcher(string);

        while (m.find()) {
            String ghostLink = m.group();
            list.add(ghostLink);
        }

        return list;
    }

    public Optional<String> findFirst(int group) {
        Matcher m = Pattern.compile(pattern)
                .matcher(string);

        m.find();

        return Optional.ofNullable(m.group(group));
    }


}
