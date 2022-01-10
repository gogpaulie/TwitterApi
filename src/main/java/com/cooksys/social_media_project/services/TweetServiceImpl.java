package com.cooksys.social_media_project.services;

import com.cooksys.social_media_project.dtos.*;
import static java.util.function.Predicate.not;

import com.cooksys.social_media_project.entities.Hashtag;
import com.cooksys.social_media_project.entities.Tweet;
import com.cooksys.social_media_project.entities.User;
import com.cooksys.social_media_project.exceptions.BadRequestException;
import com.cooksys.social_media_project.exceptions.NotAuthorizedException;
import com.cooksys.social_media_project.exceptions.NotFoundException;
import com.cooksys.social_media_project.mappers.HashtagMapper;
import com.cooksys.social_media_project.mappers.TweetMapper;
import com.cooksys.social_media_project.mappers.UserMapper;
import com.cooksys.social_media_project.parsers.TweetParser;
import com.cooksys.social_media_project.repositories.HashTagRepository;
import com.cooksys.social_media_project.repositories.TweetRepository;
import com.cooksys.social_media_project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

  private final TweetRepository tweetRepository;
  private final TweetMapper tweetMapper;
  private final AuthService authService;
  private final UserMapper userMapper;
  private final UserRepository userRepository;
  private final HashtagMapper hashtagMapper;
  private final HashTagRepository hashTagRepository;
  private final TweetParser tweetParser;

  private Tweet getTweet(Long id) {
    Optional<Tweet> tweet = tweetRepository.findById(id);
    if (!(tweet.isPresent()) || tweet.get().isDeleted()) {
      throw new NotFoundException("No tweet with such id was found");
    }
    return tweet.get();
  }

  @Override
  public List<TweetResponseDto> getAllTweets() {

    List<Tweet> tweets = tweetRepository.findAllByDeletedOrderByPosted(false);
    return tweetMapper.entitiesToResponseDtos(tweets);

  }

  @Transactional
  @Override
  public TweetResponseDto createTweet(CredentialsRequestDto credentialsRequestDto, String inputContent) {
    User user = authService.authenticate(credentialsRequestDto);
    final var content = Optional.ofNullable(inputContent).filter(s -> s.length() > 0).orElseThrow(() -> new BadRequestException("content cannot be empty"));
    final var mentions = tweetParser.parseMentions(content).stream()
            .map(u -> userRepository.findByCredentialsUsernameAndDeletedFalse(u).orElseThrow(() -> new BadRequestException(u + " does not exist")))
            .collect(Collectors.toList());
    final var hashtags = tweetParser.parseHashtags(content).stream()
            .map(label -> hashTagRepository.findOneByLabelIgnoreCase(label).orElseGet(() -> {
              final var tag = new Hashtag();
              tag.setLabel(label);
              hashTagRepository.saveAndFlush(tag);
              return tag;
            }))
            .collect(Collectors.toList());

    final var tweet = new Tweet();
    tweet.setAuthor(user);
    tweet.setContent(content);
    tweet.setHashtags(hashtags);
    tweet.setUserMentioned(mentions);
    tweetRepository.saveAndFlush(tweet);
    return tweetMapper.entityToResponseDto(tweet);
  }

  @Override
  public TweetResponseDto getTweetById(Long id) {
    Tweet tweet = getTweet(id);
    return tweetMapper.entityToResponseDto(tweet);
  }

  @Override
  public TweetResponseDto deleteTweet(Long id, CredentialsRequestDto credentialsRequestDto) {
    User user = authService.authenticate(credentialsRequestDto);
    Tweet tweet = tweetRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundException("This tweet does not exist"));

    if (user.getId().longValue() != tweet.getAuthor().getId().longValue()) {
      throw new NotAuthorizedException("User is not authorized");
    }
    tweet.setDeleted(true);
    tweetRepository.saveAndFlush(tweet);
    return tweetMapper.entityToResponseDto(tweet);

  }

  @Override
  public void addLike(Long id, CredentialsRequestDto credentialsRequestDto) {
      User user = authService.authenticate(credentialsRequestDto);
      tweetRepository.findByIdAndDeletedFalse(id).ifPresentOrElse(tweet -> {
        tweet.getLikes().add(user);
        tweetRepository.save(tweet);
      }, () -> {
        throw new NotFoundException("Tweet not found");
      });
  }

  @Override
  @Transactional
  public TweetResponseDto addReply(Long id, CredentialsRequestDto credentialsRequestDto, String inputContent) {
      User user = authService.authenticate(credentialsRequestDto);
      Tweet tweet = getTweet(id);

    final var content = Optional.ofNullable(inputContent).filter(s -> s.length() > 0).orElseThrow(() -> new BadRequestException("Empty content"));
    final List<User> mentions = tweetParser.parseMentions(content).stream()
            .map(u -> userRepository.findByCredentialsUsernameAndDeletedFalse(u).orElseThrow(() -> new BadRequestException(u + " does not exist")))
            .collect(Collectors.toList());
    final List<Hashtag> hashtags = tweetParser.parseHashtags(content).stream()
            .map(label -> hashTagRepository.findOneByLabelIgnoreCase(label).orElseGet(() -> {
              final var tag = new Hashtag();
              tag.setLabel(label);
              hashTagRepository.saveAndFlush(tag);
              return tag;
            }))
            .collect(Collectors.toList());
    final Tweet newTweet = new Tweet();
    newTweet.setAuthor(user);
    newTweet.setContent(content);
    newTweet.setHashtags(hashtags);
    newTweet.setUserMentioned(mentions);
    Tweet persisted = tweetRepository.saveAndFlush(newTweet);
    tweet.getReplies().add(persisted);
    persisted.setInReplyTo(tweet);

    return tweetMapper.entityToResponseDto(persisted);
  }

  @Override
  public TweetResponseDto createRepost(Long id, CredentialsRequestDto credentialsRequestDto) {
    User user = authService.authenticate(credentialsRequestDto);
    Tweet oldTweet = tweetRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundException("Tweet does not exist"));
    Tweet newTweet = new Tweet();
    newTweet.setAuthor(user);
    newTweet.setRepostOf(oldTweet);
    return tweetMapper.entityToResponseDto(tweetRepository.saveAndFlush(newTweet));
  }

  @Override
  public List<HashTagDto> getTagsByTweetId(Long id) {
    Tweet tweet = getTweet(id);
    List<Hashtag> hashTags = tweet.getHashtags();
    return hashtagMapper.entitiesToResponseDtos(hashTags);
  }

  @Override
  public List<UserResponseDto> getUsersWhoLikedTweet(Long id) {
    Tweet tweet = getTweet(id);
    List<User> allLikes = tweet.getLikes();
    List<User> likesFromUsersNotDeleted = new ArrayList<>();
    for (User user : allLikes) {
      if (!user.isDeleted()) {
        likesFromUsersNotDeleted.add(user);
      }
    }
    return userMapper.entitiesToResponseDtos(likesFromUsersNotDeleted);
  }

  @Override
  public ContextDto getContext(Long id) {
    Tweet targetTweet = tweetRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new NotFoundException("Tweet does not exist"));
    List<Tweet> before = new LinkedList<>();

    for (Tweet currentTweet = targetTweet.getInReplyTo(); currentTweet != null; currentTweet = currentTweet.getInReplyTo()) {
      before.add(currentTweet);
    }

    final ArrayList<Tweet> after = new ArrayList<>();

    for (Tweet tweet : targetTweet.getReplies()) {
      after.add(tweet);
    }

    ContextDto contextDto = new ContextDto();
    contextDto.setBefore(before.stream().filter((m) -> !m.isDeleted()).sorted(Comparator.comparing(Tweet::getPosted)).map(tweetMapper::entityToResponseDto).collect(Collectors.toList()));

    contextDto.setAfter(after.stream().filter((m) -> !m.isDeleted()).sorted(Comparator.comparing(Tweet::getPosted)).map(tweetMapper::entityToResponseDto).collect(Collectors.toList()));

    contextDto.setTarget(tweetMapper.entityToResponseDto(targetTweet));
    return contextDto;
  }

  @Override
  public List<TweetResponseDto> getReplies(Long id) {
    Tweet tweet = getTweet(id);

    List<Tweet> tweets = tweet.getReplies().stream().filter((t) -> !t.isDeleted()).collect(Collectors.toList());
    return tweetMapper.entitiesToResponseDtos(tweets);
  }

  @Override
  public List<TweetResponseDto> getReposts(Long id) {
    Tweet tweet = getTweet(id);
    List<Tweet> reposts = tweet.getReposts().stream().filter(not(Tweet::isDeleted)).collect(Collectors.toList());

    return tweetMapper.entitiesToResponseDtos(reposts);
  }

  @Override
  public List<UserResponseDto> getMentions(Long id) {
      Tweet tweet = getTweet(id);
      List<User> users = tweet.getUserMentioned().stream().filter(not(User::isDeleted)).collect(Collectors.toList());
      return userMapper.entitiesToResponseDtos(users);
  }

}
