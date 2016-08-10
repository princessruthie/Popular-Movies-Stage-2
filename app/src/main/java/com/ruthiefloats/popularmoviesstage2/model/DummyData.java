package com.ruthiefloats.popularmoviesstage2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Some placeholder data for when the dev doesn't have internet.
 */
public class DummyData {
    /**
     * Returns a List<Movie> that can be used to make a MovieImageAdapter
     */
    public static List<Movie> getDummyData() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112));
        movies.add(new Movie("Blah", "2016", "/hzjcILBTtVYjVCscmBonnE5wdUX.jpg", 4.7, "Stuff happens", 209112));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112));
        movies.add(new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112));

        return movies;
    }

    public static Movie getSingleDummyDatum() {
        return new Movie("Blah", "2016", "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg", 4.7, "Stuff happens", 209112);
    }

    public static List<String> getDummyReviews() {
        List<String> reviews = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            reviews.add("blah blah " + i);
        }
        return reviews;
    }

    public static String DummyJson = "{\"adult\":false,\"backdrop_path\":\"/AoT2YrJUJlg5vKE3iMOLvHlTd3m.jpg\",\"belongs_to_collection\":{\"id\":31562,\"name\":\"The Bourne Collection\",\"poster_path\":\"/lTfOZ25PhQkmGb5NHmaFnwB7b13.jpg\",\"backdrop_path\":\"/vA5xMglyZv7yzDTj1qUTU4OvelV.jpg\"},\"budget\":120000000,\"genres\":[{\"id\":28,\"name\":\"Action\"},{\"id\":53,\"name\":\"Thriller\"}],\"homepage\":\"http://www.jasonbournemovie.com\",\"id\":324668,\"imdb_id\":\"tt4196776\",\"original_language\":\"en\",\"original_title\":\"Jason Bourne\",\"overview\":\"The most dangerous former operative of the CIA is drawn out of hiding to uncover hidden truths about his past.\",\"popularity\":18.635817,\"poster_path\":\"/4xsLMMxjhlpb0MecUkGTRoZsb8b.jpg\",\"production_companies\":[{\"name\":\"Universal Pictures\",\"id\":33}],\"production_countries\":[{\"iso_3166_1\":\"US\",\"name\":\"United States of America\"}],\"release_date\":\"2016-07-27\",\"revenue\":0,\"runtime\":123,\"spoken_languages\":[{\"iso_639_1\":\"en\",\"name\":\"English\"}],\"status\":\"Released\",\"tagline\":\"You know his name\",\"title\":\"Jason Bourne\",\"video\":false,\"vote_average\":5.1,\"vote_count\":343,\"reviews\":{\"page\":1,\"results\":[{\"id\":\"579cf601925141066e000c65\",\"author\":\"Screen Zealots\",\"content\":\"A SCREEN ZEALOTS REVIEW www.screenzealots.com\\r\\n\\r\\n**LOUISA SAYS:**\\r\\n\\r\\n“Jason Bourne” is a spy movie for imbeciles. The entire film feels like it’s written using nothing more than the vocabulary of a 12 year old and consists of two very tiring hours of repetition. Bourne gets chased, throws some punches, and gets away. Shoot, bleed, run, escape. Shoot, bleed, run, escape. Shoot, bleed, run, escape. Repeat to infinity.\\r\\n\\r\\nI actually felt bad for the actors having to deliver such dreadful dialogue; their onscreen characters literally describe everything that’s happening as it unfolds (“It’s Bourne!” and “I’m going to shoot!” and “He’s running upstairs!” and “The files are downloaded!”). At some point it started to get funny.\\r\\n\\r\\nMatt Damon is back as Jason Bourne and it feels like he’s sleepwalking through the entire movie. Even the talented Alicia Vikander phones in her questionable performance (is she supposed to have an accent or not?) and Tommy Lee Jones plays yet another scowling caricature of a sinister government official. There’s little in the way of character development and the only actor who’s enjoyable here is franchise veteran Julia Stiles. What a pity that she’s not given much to do.\\r\\n\\r\\nEven the action sequences are inexcusably incoherent. Paul Greengrass is one of my least favorite directors, mainly because he loves that fast cutting junk where I can’t tell what is going on in the movie. It’s a filmmaking style for those with short attention spans and it’s a sign of extreme laziness.\\r\\n\\r\\nGreengrass sucks all the fun out of what should’ve been a spectacular car chase down the Las Vegas strip. Instead of taking his time and showing off the pageantry of stunt driving with a steady hand (see the legendary cinematic car chases in Quentin Tarantino’s “Death Proof,” William Friedkin’s “The French Connection,” Peter Yates’ “Bullit,” Justin Lin’s “Fast Five,” or hell, even Michael Bay’s “Bad Boys II“), Greengrass once again opts for the lazy way out and gives us a messy commotion of three second snippets that seem to be edited together in a blender on the high setting.\\r\\n\\r\\nNone of the elements work: the film covers no new ground, it lacks any energy, and it simply feels tired, making “Jason Bourne” the lamest of all in the series.\\r\\n\\r\\n**MATT SAYS:**\\r\\n\\r\\n“Conversation” with 5-word sentences using spy and techno-jargon. Quick cut to person typing on computer: Beep, boop, beep. Quick cut to shaky cam conversation. Another five-word-sentence conversation and more shaky cam. Cut to shaky-cam motorcycle chase with no sense of geography. Cut back to computer.\\r\\n\\r\\nCut, cut, cut. Shaky cam, shaky cam, shaky cam. “Jason Bourne” might as well have been shot and assembled by a seven-year-old with ADD that hasn’t taken his Ritalin. It wasn’t so much edited as jammed together. So little artistry went into making this movie that it’s hard to even call Paul Greengrass its “director.”\\r\\n\\r\\nOne of my recurring rants is on the use of quick cutting and shaky cams in action films: it’s the hallmark of lazy filmmaking. When your action sequences are constructed by using cut after cut after cut, you don’t have to worry about storyboarding (contrast “The Raid: Redemption“). You don’t need actors who have any training in fight choreography (contrast “The Raid 2“). You don’t have to concern yourself with geography or spatial relationships. In other words, instead of having to WORK at creating a compelling action sequence, you can hack your way through it. And boy, there is NO ONE working in film now that loves hack action better than Paul Greengrass. And nowhere has Greengrass’s hackiness been on display more than in “Jason Bourne.” It’s his masterpiece of hacketry. I can continue making up new word forms using “hack” to describe this movie and director, but I think you get the idea.\\r\\n\\r\\nIn addition to the bad direction and editing, “Jason Bourne” stinks because it’s a poor excuse for a spy thriller. We are subjected to scene after scene of dreadful acting. Julia Stiles (Nicky Parsons) is the worst of the lot, but Matt Damon (Jason Bourne), Alicia Vikander (Heather Lee) and Tommy Lee Jones (Director Dewey) are only marginally better. The script is abysmal, with the characters not so much dialoguing with one another as speaking spy techno-jargon while they type on computers that are constantly beep-bloop-bleeping (no computer I’ve ever used makes so many noises when scanning files). Using words that sound cool does not make a scene interesting. And the plot? It’s barely even there.\\r\\n\\r\\nI found only three things enjoyable about this movie. The very first fight scene between Bourne and some nameless guy — the one you see in the trailer. The story thread featuring the Silicon Valley billionaire that refused to screw over the public in the name of national security. And the final vehicular chase scene down Las Vegas Boulevard — which I liked in spite of the terrible editing (which, incidentally, got the geography of the Strip all wrong).\\r\\n\\r\\nPlease don’t make this movie a hit, because then we will get lots of imitators (like we did after “The Bourne Supremacy” and “The Bourne Ultimatum“, when quick cuts and shaky cam were used in 95% of all action pictures).\\r\\n\\r\\nDemand more for your money. There are so many movies that do it better than this one. Do you want an engaging, twisty techno-spy thriller? Check out the “Mission Impossible” series. Do you want a well-written story of international espionage and intrigue? See “Our Kind of Traitor.” Do you want well-choreographed fight sequences? Watch “The Raid” movies. Hell, even this summer’s “Warcraft” did a better job with its fights and action that this film.\\r\\n\\r\\n**A SCREEN ZEALOTS REVIEW www.screenzealots.com**\",\"url\":\"https://www.themoviedb.org/review/579cf601925141066e000c65\"}],\"total_pages\":1,\"total_results\":1},\"videos\":{\"results\":[{\"id\":\"56b7f59ac3a36806ec00fdcf\",\"iso_639_1\":\"en\",\"iso_3166_1\":\"US\",\"key\":\"_gBnmKOixDM\",\"name\":\"First Look\",\"site\":\"YouTube\",\"size\":1080,\"type\":\"Clip\"},{\"id\":\"571a47bfc3a3682553001bd7\",\"iso_639_1\":\"en\",\"iso_3166_1\":\"US\",\"key\":\"pq1UChDxlvw\",\"name\":\"Official trailer 2\",\"site\":\"YouTube\",\"size\":1080,\"type\":\"Trailer\"},{\"id\":\"5718a3869251417a220092ab\",\"iso_639_1\":\"en\",\"iso_3166_1\":\"US\",\"key\":\"F4gJsKZvqE4\",\"name\":\"Official Trailer\",\"site\":\"YouTube\",\"size\":1080,\"type\":\"Trailer\"}]}}";
}
