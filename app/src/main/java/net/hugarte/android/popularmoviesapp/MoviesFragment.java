package net.hugarte.android.popularmoviesapp;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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

/*
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment {

    private final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private GridView moviesView;
    private MoviesAdapter moviesAdapter;

    public MoviesFragment() {
        // Required empty public constructor
    }

    /*/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesFragment.
     */

    /*public static MoviesFragment newInstance(String param1, String param2) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        //String[] movies = {"The Martian", "Interstellar", "Suicide squat", "The lion King", "Peli de prueba"};
        moviesView = (GridView) rootView.findViewById(R.id.moviesContainer);






        return rootView;
    }

    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute();
    }


    public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<MovieInfo>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w342/";
        @Override
        protected ArrayList<MovieInfo> doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String baseUrl = "http://api.themoviedb.org/3/movie/popular";
                final String QUERY_PARAM = "api_key";


                Uri builtUri = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());


                //String apiKey = "&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                //URL url = new URL(baseUrl.concat(apiKey));
                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.d(LOG_TAG, "Movies JSON String: " + moviesJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson(moviesJsonStr);
            } catch (Exception e) {
                e.printStackTrace();

            }
            /*catch (JSONException e) {

                e.printStackTrace();
            }*/
            return null;
        }

        private ArrayList<MovieInfo> getMovieDataFromJson(String moviesJsonStr) throws JSONException {

            //Create list to store the results
            ArrayList<MovieInfo> resultList = new ArrayList<MovieInfo>();

            //Constants to extract JSON nodes
            final String RESULTS = "results";
            final String POSTER_PATH = "poster_path";
            final String ID = "id";
            final String RELEASE_DATE = "release_date";
            final String OVERVIEW = "overview";
            final String ORIGINAL_TITLE = "original_title";
            final String VOTE_AVERAGE = "vote_average";


            //Create JSON Object from input parameter and JSON array of results from JSONObject
            JSONObject moviesJSON = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJSON.getJSONArray(RESULTS);

            //iterate over the results array to extract desired nodes and add to the result list;
            for (int i = 0; i < moviesArray.length(); i++) {
                MovieInfo movieInfo = new MovieInfo();
                JSONObject movieJSONObject = moviesArray.getJSONObject(i);
                movieInfo.setPoster_path(BASE_POSTER_URL + movieJSONObject.getString(POSTER_PATH));
                movieInfo.setId(movieJSONObject.getString(ID));
                movieInfo.setRelease_date(movieJSONObject.getString(RELEASE_DATE));
                movieInfo.setOverview(movieJSONObject.getString(OVERVIEW));
                movieInfo.setOriginal_title(movieJSONObject.getString(ORIGINAL_TITLE));
                movieInfo.setVote_average(movieJSONObject.getString(VOTE_AVERAGE));
                resultList.add(movieInfo);
            }
            return resultList;
        }
        @Override
        protected void onPostExecute(ArrayList<MovieInfo> strings) {
            moviesAdapter = new MoviesAdapter(getActivity(),strings);
            moviesView.setAdapter(moviesAdapter);
            super.onPostExecute(strings);
        }


    }
}



