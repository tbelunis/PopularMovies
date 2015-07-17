package org.example.popularmovies.app;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends Fragment {
    private static final String TAG = "PopularMoviesFragment";

    private ArrayList<PopularMoviesResult> mResults = new ArrayList<PopularMoviesResult>();
    private PopularMoviesAdapter mAdapter;
    private String mSortOrder;

    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_movies, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.popular_movies_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mSortOrder = isSortByRating() ? Constants.SORT_BY_RATING : Constants.SORT_BY_POPULARITY;

        mAdapter = new PopularMoviesAdapter();
        recyclerView.setAdapter(mAdapter);
        PopularMoviesTask task = new PopularMoviesTask();
        task.execute();
    }

    private void loadResults(String jsonResponse) {
        mResults.clear();
        try {
            JSONObject object = new JSONObject(jsonResponse);
            JSONArray array = object.getJSONArray(Constants.RESULTS);
            for (int i = 0; i < array.length(); i++) {
                mResults.add(new PopularMoviesResult(array.getJSONObject(i)));
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private boolean isSortByRating() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getString(getActivity().getString(R.string.default_sort_pref), "")
                .equals(getActivity().getString(R.string.pref_sort_type));
    }

    private class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesViewHolder> {

        @Override
        public PopularMoviesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.popular_movie_list_item, viewGroup, false);
            return new PopularMoviesViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PopularMoviesViewHolder popularMoviesViewHolder, int i) {
            PopularMoviesResult result = mResults.get(i);
            String imageUrl = result.getPosterPath();
            Picasso.with(getActivity()).load(imageUrl).into(popularMoviesViewHolder.mPosterImageView);
        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }
    }

    private class PopularMoviesViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private ImageView mPosterImageView;

        public PopularMoviesViewHolder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView)itemView.findViewById(R.id.poster_imageview);
            mPosterImageView.setAdjustViewBounds(true);
            mPosterImageView.setPadding(0,0,0,0);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private class PopularMoviesTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String jsonResponseString = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                Uri builtUri = Uri.parse(Constants.POPULAR_MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(Constants.API_KEY_PARAM, Constants.THEMOVIEDB_API_KEY)
                        .appendQueryParameter(Constants.SORT_BY_PARAM, mSortOrder)
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                jsonResponseString = buffer.toString();
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return jsonResponseString;
        }

        @Override
        protected void onPostExecute(String s) {
            loadResults(s);
        }
    }
}
