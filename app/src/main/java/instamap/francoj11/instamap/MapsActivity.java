package instamap.francoj11.instamap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.internal.di;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng mLatLng; // The current Latitude and Longitude
    private int distance;   // The current distance to the current LatLng

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("mLatLng",mLatLng);
        outState.putInt("distance",distance);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /*If there is no savedInstance state, then we ask for the LastKnownLocation to have
        * an estimate aproximation to user's location. If there is not LastKnowLocation (i.e. ,
        * the user has just reboot the phone/tablet) then we set the location to Eiffel Tower, in
        * Paris, France.
        **/
        if (savedInstanceState == null || !savedInstanceState.containsKey("distance")){
            System.out.println("Creating from 0");
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria,true);
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                mLatLng = new LatLng(48.857557, 2.295594);
            }
            distance = 500;
        } else {
            System.out.println("Restoring state");
            mLatLng = savedInstanceState.getParcelable("mLatLng");
            distance = savedInstanceState.getInt("distance");
        }
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_confirm){
            startPhotosActivity();
            return true;
        } else if (id == R.id.action_edit){
            editDistance();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Starts the PhotosActivity activity
    public void startPhotosActivity(){
        Intent intent = new Intent(this,PhotosActivity.class);
        Bundle b = new Bundle();

        b.putParcelable("LatLng",mLatLng);
        b.putInt("Distance",distance);
        intent.putExtra("bundle",b);

        startActivity(intent);
    }

    /* Prompts a custom AlertDialog to enter a new radius */
    public void editDistance(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("New radius:");
        alert.setMessage("Set the radius (in meters) of the searcheable area\n"
                        +"Min distance: 1m\n"
                        +"Max distance: 5000m");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);

        alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    distance = Integer.parseInt(input.getText().toString());
                    setUpMapWithCircle(mLatLng,distance);
                } catch(Exception e){
                    Toast.makeText(MapsActivity.this,"Please enter a valid number",Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                //On click, we show the market and circle in the touched point.
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        setUpMapWithCircle(latLng,distance);
                    }
                });
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        setUpMapWithCircle(latLng,distance);
                    }
                });
                setUpMapWithCircle(mLatLng,distance);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));

            }
        }
    }

    // Puts a Marker and a circle with Radius radius in the specified Latitude & longitude
    private void setUpMapWithCircle(LatLng latLng,int radius){
        CircleOptions co;

        co = new CircleOptions();
        co.center(latLng);
        co.radius(radius);
        co.fillColor(0x6400cc00);
        co.strokeColor(0xff00cc00);
        co.strokeWidth(5);

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.addCircle(co);

        mLatLng = latLng;
    }
}
