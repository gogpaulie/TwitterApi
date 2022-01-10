package com.cooksys.social_media_project.parsers;

import java.util.Set;

public interface TweetParser {
    public Set<String> parseMentions(String input);
    public Set<String> parseHashtags(String input);

}
