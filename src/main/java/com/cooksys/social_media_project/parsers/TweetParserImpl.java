package com.cooksys.social_media_project.parsers;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TweetParserImpl implements TweetParser {
    private final PrefixParser mentions = new PrefixParserImpl("@");
    private final PrefixParser hashtags = new PrefixParserImpl("#");

    @Override
    public Set<String> parseMentions(String input) {
        return mentions.parse(input);
    }

    @Override
    public Set<String> parseHashtags(String input) {
        return hashtags.parse(input);
    }
}
