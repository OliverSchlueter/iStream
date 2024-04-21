package de.itbw18.istream.streamconfig;

public record StreamConfig(String userId, String title, String description, Category category, String[] tags, String language) {

    public enum Category {
        IRL,
        MUSIC,
        CASINO,
        HOT_TUB,
        JUST_CHATTING,
        SCIENCE_AND_TECHNOLOGY,
        SPORTS,
        TRAVEL_AND_OUTDOORS,
        TALK_SHOW,
        GAME,
        ART,
        FOOD_AND_DRINK,
        NEWS,
        ANIME,
        MOVIE,
        ;
    }

}
