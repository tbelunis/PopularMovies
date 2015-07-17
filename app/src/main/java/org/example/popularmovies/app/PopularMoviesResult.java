package org.example.popularmovies.app;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Tom on 7/11/2015.
 */
public class PopularMoviesResult {
    private long mId;
    private String mOriginalTitle;
    private String mPosterPath;
    private String mOverview;
    private int mUserRating;
    private Date mReleaseDate;

    public PopularMoviesResult(JSONObject json) {
        try {
            mId = json.getLong(Constants.ID);
            mOriginalTitle = json.getString(Constants.ORIGINAL_TITLE);
            mOverview = json.getString(Constants.OVERVIEW);
            mPosterPath = json.getString(Constants.POSTER_PATH);
            mUserRating = json.getInt(Constants.VOTE_AVERAGE);
            mReleaseDate = Constants.DATE_FORMAT.parse(json.getString(Constants.RELEASE_DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public int getUserRating() {
        return mUserRating;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public long getId() {

        return mId;
    }

    public String getPosterPath() {
        return Constants.POSTER_BASE_URL + Constants.DEFAULT_IMAGE_SIZE + "/" + mPosterPath;

    }
}
