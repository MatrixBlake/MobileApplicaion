package comp5216.sydney.edu.au.homework3;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private TextView locationTextView;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation = new Location("");

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private double oldlongti;
    private double oldlati;
    private double newlongti;
    private double newlati;
    double distances;
    double times;
    double pace;
    double speed;

    TextView rundetail;

    private Button btnStart;

    final Handler handler = new Handler();
    final int delay = 1000;
    Runnable runnable;

    ArrayList<RunningRecords> items;
    RecordItemDao recordItemDao;
    RecordItemDB db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        btnStart=findViewById(R.id.btnstart);
        rundetail=findViewById(R.id.rundetail);

        runnable = new Runnable() {
            public void run(){

                getDeviceLocation();
                Location loc1 = new Location("");
                loc1.setLatitude(oldlati);
                loc1.setLongitude(oldlongti);
                Log.i("AAA","old"+oldlati+" "+oldlongti);
                Location loc2 = new Location("");
                loc2.setLatitude(newlati);
                loc2.setLongitude(newlongti);
                Log.i("AAA","new"+newlati+" "+newlongti);

                float distanceInMeters = loc1.distanceTo(loc2);
                distances+=distanceInMeters/1000;
                times=times+1;
                rundetail.setText("The distances is "+distances+". The time is "+times);
                Log.i("AAA",distances+"");
                oldlati=newlati;
                oldlongti=newlongti;
                handler.postDelayed(this, delay);
            }
        };



        // Obtain the location TextView
        locationTextView = (TextView) this.findViewById(R.id.location);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Obtain the current location of the device
                            mLastKnownLocation = task.getResult();
                            String currentOrDefault = "Current";

                            // Check whether the current location is null
                            if (mLastKnownLocation != null) {
                                // Animate the map's camera position to the current location of the device
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                // Animate the map's camera position to the default location instead
                                Log.d(TAG, "Current location is null. Using defaults.");
                                currentOrDefault = "Default";
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);

                                // Set current location to the default location
                                mLastKnownLocation = new Location("");
                                mLastKnownLocation.setLatitude(mDefaultLocation.latitude);
                                mLastKnownLocation.setLongitude(mDefaultLocation.longitude);
                            }

                            // Show location details on the location TextView
                            String msg = currentOrDefault + " Location: " +
                                    Double.toString(mLastKnownLocation.getLatitude()) + ", " +
                                    Double.toString(mLastKnownLocation.getLongitude());
                            newlati=mLastKnownLocation.getLatitude();
                            newlongti=mLastKnownLocation.getLongitude();
                            locationTextView.setText(msg);

                            // Add a marker for my current location on the map
//                            MarkerOptions marker = new MarkerOptions().position(
//                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()))
//                                    .title("I am here");
//                            mMap.addMarker(marker);
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true); // false to disable my location button
                mMap.getUiSettings().setZoomControlsEnabled(true); // false to disable zoom controls
                mMap.getUiSettings().setCompassEnabled(true); // false to disable compass
                mMap.getUiSettings().setRotateGesturesEnabled(true); // false to disable rotate gesture
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void onOpenCalculator(View view){
        Intent intent = new Intent(MapsActivity.this, Calculator.class);
        this.startActivityForResult(intent,999);
    }

    public void onOpenLog(View view){
        Intent intent = new Intent(MapsActivity.this, LogHistory.class);
        this.startActivityForResult(intent,998);
    }

    public void onRunStart(View v){
        btnStart.setVisibility(View.INVISIBLE);
        distances=0;
        times=0;
        oldlati=newlati;
        oldlongti=newlongti;
        handler.postDelayed(runnable, delay);

    }
    public void onRunFinish(View v){
        handler.removeCallbacks(runnable);
        btnStart.setVisibility(View.VISIBLE);
        if(distances>0){
            pace=(Math.round((times/60)/distances*100))/100.0;
            speed=(Math.round(distances/times*60*60*100))/100.0;

            db = RecordItemDB.getDatabase(this.getApplication().getApplicationContext());
            recordItemDao = db.recordItemDao();
            readItemsFromDatabase();

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(date);

            items.add(new RunningRecords(dateString,Math.round(100*times/60)/100.0,Math.round(distances*100)/100.0,pace,speed));
            saveItemsToDatabase();
        }

    }


    private void readItemsFromDatabase()
    {
//Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
//read items from database
                    List<RecordItem> itemsFromDB = recordItemDao.listAll();
                    items = new ArrayList<RunningRecords>();
                    if (itemsFromDB != null & itemsFromDB.size() > 0) {
                        for (RecordItem item : itemsFromDB) {
                            items.add(new RunningRecords(item.getRundate(),item.getRuntime(),item.getDistance(),item.getPace(),item.getSpeed()));
                        }
                    }
                    return null;
                }
            }.execute().get();
        }
        catch(Exception ex) {}
    }
    private void saveItemsToDatabase()
    {
//Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
//delete all items and re-insert
                recordItemDao.deleteAll();
                for (RunningRecords todo : items) {
                    RecordItem item = new RecordItem(todo.getRundate(),todo.getRuntime(),todo.getDistance(),todo.getPace(),todo.getSpeed());
                    recordItemDao.insert(item);
                }
                return null;
            }
        }.execute();
    }


}
