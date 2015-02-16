package instamap.francoj11.instamap;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;


public class PhotosActivity extends ActionBarActivity {

    private static final String LOGTAG = "PHOTOS";
    private ProgressBar progressBar;
    private LatLng mLatLng;
    private int distance;
    private int radio;
    private ArrayList<PhotoPost> photoList;
    private String instagramAPI = "https://api.instagram.com/v1/media/search?";
    private String instaClientId = "YOUR_INSTAGRAM_KEY_ID";
    private final int COUNT = 100;

    //RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        photoList = new ArrayList<PhotoPost>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.my_progressbar);

        if (i.hasExtra("bundle")){
            mLatLng = i.getBundleExtra("bundle").getParcelable("LatLng");
            distance = i.getBundleExtra("bundle").getInt("Distance");
        }

        String stringURL = instagramAPI + "lat=" + mLatLng.latitude + "&lng=" + mLatLng.longitude +
                            "&distance=" + distance + "&count=" + COUNT +
                            "&client_id=" + instaClientId;

        Log.d(LOGTAG,stringURL);
        new DownloadWebpageTask().execute(stringURL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    // Downloads an Instagram request, and the result is parsed as a Json object
    private class DownloadWebpageTask extends AsyncTask<String, Void, Void> {

        protected void onPreExecute() {
            Log.d(LOGTAG, "PREEXECUTE");
        }

        @Override
        protected Void doInBackground(String... urls) {
            try {
                Log.d(LOGTAG,"DOWNLOADING DATA");
                downloadUrl(urls[0]);
            } catch (IOException e){
                Log.d(LOGTAG,"ERROR DOWNLOADING DATA");
            }
            return null;
        }

        protected void onPostExecute(Void v){
            Log.d(LOGTAG,"FINISHED DOWNLOAD");
            mAdapter = new MyAdapter(photoList,PhotosActivity.this);
            mRecyclerView.setAdapter(mAdapter);

            progressBar.setVisibility(View.INVISIBLE);
        }

        private void downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(LOGTAG, "The response is: " + response);
                is = conn.getInputStream();

                BufferedReader reader  = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }
                Parse(sb.toString());
                return;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            }
            catch (Exception e){
                Log.d(LOGTAG,"ERROR");
                e.printStackTrace();
            }
            finally {
                if (is != null) {
                    is.close();
                    Log.d(LOGTAG,"INPUTSTREAM CLOSED");
                }
            }
            return;
        }

        /*Parses the Instagram response, which is in JSON format, then it creates PhotoPost objects
         * which are added to the photoList.
         * */
        public void Parse(String s) throws JSONException {
            JSONTokener jt = new JSONTokener(s);
            JSONObject jo;
            JSONArray ja;

            jo = (JSONObject) jt.nextValue();
            ja = jo.getJSONArray("data");

            for (int i = 0; i < ja.length(); i++) {
                JSONObject o = ja.getJSONObject(i);
                StringBuilder sb = new StringBuilder();
                PhotoPost pp = new PhotoPost();

                if (o.getString("type").compareToIgnoreCase("image") == 0){
                    JSONObject oAux = o.getJSONObject("images");
                    oAux = oAux.getJSONObject("standard_resolution");
                    sb.append(o.getString("link") + " " + oAux.getString("url") + " ");
                    pp.setImageURL(oAux.getString("url"));
                    Log.d(LOGTAG, "1");
                    oAux = o.getJSONObject("user");
                    Log.d(LOGTAG,"2");
                    sb.append(oAux.getString("username") + " " + oAux.getString("profile_picture"));
                    pp.setUsername(oAux.getString("username"));
                    pp.setUserProfilePicture(oAux.getString("profile_picture"));
                    Log.d(LOGTAG,"3");
                    photoList.add(pp);
                }
                Log.d(LOGTAG,sb.toString());
            }

            Log.d(LOGTAG,"NUMBER OF PHOTOS: " + ja.length());
        }
    }
}
