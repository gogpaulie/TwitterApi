package com.cooksys.social_media_project;

import com.cooksys.social_media_project.entities.*;
import com.cooksys.social_media_project.repositories.HashTagRepository;
import com.cooksys.social_media_project.repositories.TweetRepository;
import com.cooksys.social_media_project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {

    private final HashTagRepository hashTagRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        // --- USERS ---

        // USER 1
        Credentials user1Cred = new Credentials();
        user1Cred.setUsername("User1");
        user1Cred.setPassword("Password");

        User user1 = new User();
        user1.setCredentials(user1Cred);

        // Profile
        Profile user1Profile = new Profile();
        user1Profile.setFirstName("John");
        user1Profile.setLastName("Doe");
        user1Profile.setEmail("user1@email.com");
        user1Profile.setPhone("123-456-7890");
        user1.setProfile(user1Profile);
        // Deleted
        user1.setDeleted(false);
        userRepository.saveAndFlush(user1);

        // USER 2
        Credentials user2Cred = new Credentials();
        user2Cred.setUsername("User2");
        user2Cred.setPassword("Password");

        User user2 = new User();
        user2.setCredentials(user2Cred);

        // Profile
        Profile user2Profile = new Profile();
        user2Profile.setFirstName("Ned");
        user2Profile.setLastName("Flanders");
        user2Profile.setEmail("user2@email.com");
        user2Profile.setPhone("555-555-5555");
        user2.setProfile(user2Profile);
        // Deleted
        user2.setDeleted(false);
        userRepository.saveAndFlush(user2);

        // USER 3
        Credentials user3Cred = new Credentials();
        user3Cred.setUsername("User3");
        user3Cred.setPassword("Password");

        User user3 = new User();
        user3.setCredentials(user3Cred);

        // Profile
        Profile user3Profile = new Profile();
        user3Profile.setFirstName("Homer");
        user3Profile.setLastName("Simpson");
        user3Profile.setEmail("user3@email.com");
        user3Profile.setPhone("444-444-4444");
        user3.setProfile(user3Profile);
        // Deleted
        user3.setDeleted(false);
        userRepository.saveAndFlush(user3);

        // USER 4
        Credentials user4Cred = new Credentials();
        user4Cred.setUsername("User4");
        user4Cred.setPassword("Password");

        User user4 = new User();
        user4.setCredentials(user4Cred);

        // Profile
        Profile user4Profile = new Profile();
        user4Profile.setFirstName("Bart");
        user4Profile.setLastName("Simpson");
        user4Profile.setEmail("user4@email.com");
        user4Profile.setPhone("333-333-3333");
        user4.setProfile(user4Profile);
        // Deleted
        user4.setDeleted(false);
        userRepository.saveAndFlush(user4);

        // USER 5
        Credentials user5Cred = new Credentials();
        user5Cred.setUsername("User5");
        user5Cred.setPassword("Password");

        User user5 = new User();
        user5.setCredentials(user5Cred);

        // Profile
        Profile user5Profile = new Profile();
        user5Profile.setFirstName("Ralph");
        user5Profile.setLastName("Wiggum");
        user5Profile.setEmail("user5@email.com");
        user5Profile.setPhone("222-222-2222");
        user5.setProfile(user5Profile);
        // Deleted
        user5.setDeleted(false);
        userRepository.saveAndFlush(user5);

        // DELETED USER
        Credentials deletedUserCred = new Credentials();
        deletedUserCred.setUsername("DeletedUser");
        deletedUserCred.setPassword("Password");

        User deletedUser = new User();
        deletedUser.setCredentials(deletedUserCred);

        // Profile
        Profile deletedUserPro = new Profile();
        deletedUserPro.setFirstName("Deleted");
        deletedUserPro.setLastName("User");
        deletedUserPro.setEmail("Deleted@User.com");
        deletedUserPro.setPhone("NULL");
        deletedUser.setProfile(deletedUserPro);
        // Deleted
        deletedUser.setDeleted(true);
        userRepository.saveAndFlush(deletedUser);

        // --- TWEETS ---

        // Tweet 1
        Tweet tweet1 = new Tweet();
        tweet1.setAuthor(user1);
        tweet1.setDeleted(false);
        tweet1.setContent("This is a tweet 1");
        tweetRepository.saveAndFlush(tweet1);

        // Tweet 2
        Tweet tweet2 = new Tweet();
        tweet2.setAuthor(user1);
        tweet2.setDeleted(false);
        tweet2.setContent("This is a tweet 2");
        tweetRepository.saveAndFlush(tweet2);

        // Tweet 3
        Tweet tweet3 = new Tweet();
        tweet3.setAuthor(user2);
        tweet3.setDeleted(false);
        // Set Content @PARAM String
        tweet3.setContent("This is a tweet 3");
        tweetRepository.saveAndFlush(tweet3);


        // Tweet 4
        Tweet tweet4 = new Tweet();
        tweet4.setAuthor(user2);
        tweet4.setDeleted(false);
        // Set Content @PARAM String
        tweet4.setContent("This is a tweet 4");
        tweetRepository.saveAndFlush(tweet4);

        // Tweet 5
        Tweet tweet5 = new Tweet();
        tweet5.setAuthor(user3);
        tweet5.setDeleted(false);
        // Set Content @PARAM String
        tweet5.setContent("This is a tweet 5");
        tweetRepository.saveAndFlush(tweet5);

        // Tweet 6
        Tweet tweet6 = new Tweet();
        tweet6.setAuthor(user3);
        tweet6.setDeleted(false);
        // Set Content @PARAM String
        tweet6.setContent("This is a tweet 6");
        tweetRepository.saveAndFlush(tweet6);


        //  Tweet 7
        Tweet deletedTweet = new Tweet();
        deletedTweet.setAuthor(user3);
        deletedTweet.setDeleted(true);
        // Set Content @PARAM String
        deletedTweet.setContent("This is a deleted tweet (User3)");
        tweetRepository.saveAndFlush(deletedTweet);

        // --- LIST of Tweets + Adding to User# ---
        List<Tweet> user1Tweets = List.of(tweet1, tweet2);
        user1.setTweets(user1Tweets);
        userRepository.saveAndFlush(user1);

        List<Tweet> user2Tweets = List.of(tweet3, tweet4);
        user2.setTweets(user2Tweets);
        userRepository.saveAndFlush(user2);

        List<Tweet> user3Tweets = List.of(tweet5, tweet6);
        user3.setTweets(user3Tweets);
        userRepository.saveAndFlush(user3);


        // --- List of Liked Tweets ---
        user1.setLikedTweets(user3Tweets);
        userRepository.saveAndFlush(user1);

        user2.setLikedTweets(user1Tweets);
        userRepository.saveAndFlush(user2);

        user3.setLikedTweets(user2Tweets);
        userRepository.saveAndFlush(user3);

        // --- List of Following ---
        List<User> followingList = List.of(user2, user3, user4);
        user1.setFollowing(followingList);
        userRepository.saveAndFlush(user1);
        // --- List of Followers ---
        List<User> followersList = List.of(user3, user5);
        user1.setFollowers(followersList);
        userRepository.saveAndFlush(user1);

        // --- Tweet Mentions ---
        Tweet mention1 = new Tweet();
        mention1.setAuthor(user2);
        mention1.setDeleted(false);
        Tweet mention2 = new Tweet();
        mention2.setAuthor(user3);
        mention2.setDeleted(false);
        // Set Content @PARAM String
        mention1.setContent("This is a tweet for tweet mention 1");
        mention1.setUserMentioned(List.of(user1));
        tweetRepository.saveAndFlush(mention1);
        mention2.setContent("This is a tweet for tweet mention 2");
        mention2.setUserMentioned(List.of(user1));
        tweetRepository.saveAndFlush(mention2);


        // Following
        List<User> following_1 = List.of(user2, user3, user4, deletedUser);
        user1.setFollowing(following_1);

        List<User> followers_1 = List.of(user5, deletedUser);
        user1.setFollowers(followers_1);
        userRepository.saveAndFlush(user1);

        // --- HashTags ---
        Hashtag hashtag1 = new Hashtag();
        hashtag1.setLabel("#" + "Helloworld");
        hashTagRepository.saveAndFlush(hashtag1);

        Hashtag hashtag2 = new Hashtag();
        hashtag2.setLabel("#" + "coding");
        hashTagRepository.saveAndFlush(hashtag2);
    }
}
