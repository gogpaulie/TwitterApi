package com.cooksys.social_media_project.parsers;

import java.util.Optional;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrefixParserImpl implements PrefixParser{
    private final Pattern pattern;

    public PrefixParserImpl(String prefix) {
        pattern = Pattern.compile(prefix + "([^" + prefix + "\\s]+)");
    }

    @Override
    public Set<String> parse(String input) {
        return Optional.ofNullable(input)
                .stream()
                .map(pattern::matcher)
                .flatMap(Matcher::results)
                .flatMap(safeGetGroup(1))
                .collect(Collectors.toSet());
    }

    private static Function<MatchResult, Stream<String>> safeGetGroup(int index) {
        return result -> {
            try {
                return Optional.ofNullable(result.group(index)).stream();
            } catch (Exception e) {
                return Stream.empty();
            }
        };
    }
}
