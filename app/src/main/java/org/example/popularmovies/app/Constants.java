package org.example.popularmovies.app;

import java.text.SimpleDateFormat;

/**
 * Created by Tom on 7/11/2015.
 */
public class Constants {
    public static final String POPULAR_MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String DEFAULT_IMAGE_SIZE = "w500";
    public static final String SORT_BY_PARAM = "sort_by";
    public static final String API_KEY_PARAM = "api_key";
    public static final String THEMOVIEDB_API_KEY = "INSERT_API_KEY_HERE";
    public static final String SORT_BY_RATING = "vote_average.desc";
    public static final String SORT_BY_POPULARITY = "popularity.desc";

    // JSON Response Constants
    public static final String RESULTS = "results";
    public static final String ID = "id";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String POSTER_PATH = "poster_path";
    public static final String OVERVIEW = "overview";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String RELEASE_DATE = "release_date";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd");
}
