package com.yashtawade.foodforthought.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.yashtawade.foodforthought.GooglePlacesReadTask;
import com.yashtawade.foodforthought.R;
import com.yashtawade.foodforthought.constants.FFTConstant;

public class NearbyActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final String GOOGLE_API_KEY = FFTConstant.GOOGLE_MAP_API_KEY;
    private static final String EXTRA_SEARCH_KEYWORD = "fft_search_keyword";
    double latitude;
    double longitude;
    public String keyword;
    private int PROXIMITY_RADIUS = 5000;
    SupportMapFragment fragment;

    TextView mSearchEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //show error dialog if GooglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        fragment.getMapAsync(this);

    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        Button btnFind = (Button) findViewById(R.id.btnFind);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if(location != null){
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }else{
            longitude = -97.1208;
            latitude = 32.7255;
        }

        mSearchEdit = (TextView) findViewById(R.id.search_nearby_edit_text);

        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        locationManager.requestLocationUpdates(bestProvider, 20000, 1, this);

        StringBuilder sb = buildGooglePlaceUrl();
        keyword = getIntent().getStringExtra(EXTRA_SEARCH_KEYWORD);
        sb.append("&keyword=" + keyword);
        Log.d("link: ",sb.toString());
        googlePlaceExecute(sb.toString(), googleMap);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = buildGooglePlaceUrl();
                keyword = mSearchEdit.getText().toString();
                sb.append("&keyword=" + keyword);
                Log.d("link: ",sb.toString());
                googlePlaceExecute(sb.toString(), googleMap);
            }
        });
    }

    private StringBuilder buildGooglePlaceUrl(){
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + GOOGLE_API_KEY);

        return googlePlacesUrl;
    }

    private void googlePlaceExecute(String s, GoogleMap googleMap){
        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = s;
        googlePlacesReadTask.execute(toPass);
    }

    public static Intent newIntent(Context mContext, String keyword){
        Intent i = new Intent(mContext, NearbyActivity.class);
        i.putExtra(EXTRA_SEARCH_KEYWORD, keyword);
        return i;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //do nothing
    }

    @Override
    public void onProviderEnabled(String s) {
        //do nothing
    }

    @Override
    public void onProviderDisabled(String s) {
        //do nothing
    }
}
