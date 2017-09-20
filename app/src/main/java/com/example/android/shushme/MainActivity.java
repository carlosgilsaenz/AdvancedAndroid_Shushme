package com.example.android.shushme;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks{

    private static final String TAG = MainActivity.class.getName();

    // Member variables
    private PlaceListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    //  GoogleApiClient
    private GoogleApiClient mGoogleAPIClient;

    //String Constant
    private static final String PERMISSIONS_STRING[] = {android.Manifest.permission.ACCESS_FINE_LOCATION};
    //Request Code
    private static final int PERMISSIONS_REQUEST_CODE = 101;

    // TODO (8) Implement onLocationPermissionClicked to handle the CheckBox click event
    @OnClick(R.id.enable_location)
    public void onLocationPermissionClicked(CheckBox checkbox){
        if(checkbox.isChecked()){
            ActivityCompat.requestPermissions(this,
                    PERMISSIONS_STRING, PERMISSIONS_REQUEST_CODE);
        }
        Log.d(TAG,"OnLocationPermissionClicke");
        Timber.d("Checkbox clicked");
    }

    // TODO (9) Implement the Add Place Button click event to show  a toast message with the permission status
    @OnClick(R.id.add_location)
    public void onAddNewLocationClicked(Button button) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Timber.d("Permissions ACCESS_FINE_LOCATION Enabled");
            Log.d(TAG,"Permissions ACCESS_FINE_LOCATION Enabled");
            Toast.makeText(this,"Permissions ACCESS_FINE_LOCATION Enabled",Toast.LENGTH_LONG).show();
        }else{
            Timber.d("Permissions ACCESS_FINE_LOCATION Disabled");
            Log.d(TAG,"Permissions ACCESS_FINE_LOCATION Disabled" );
            Toast.makeText(this,"Permissions ACCESS_FINE_LOCATION Disabled",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called when the activity is starting
     *
     * @param savedInstanceState The Bundle that contains the data supplied in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.places_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PlaceListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        Timber.plant(new Timber.DebugTree());

        ButterKnife.bind(this);

        // TODO (4) Create a GoogleApiClient with the LocationServices API and GEO_DATA_API
        mGoogleAPIClient = new GoogleApiClient.
                Builder(this).
                addApi(Places.GEO_DATA_API).
                addApi(Places.PLACE_DETECTION_API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                enableAutoManage(this,this).
                build();
    }

    // TODO (5) Override onConnected, onConnectionSuspended and onConnectionFailed for GoogleApiClient
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.e("Google API: Connection Failed");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.d("Google API: Connection Successful");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.d("Google API:  Connection Suspended");
    }

    // TODO (7) Override onResume and inside it initialize the location permissions checkbox
    @Override
    protected void onResume() {
        super.onResume();

        CheckBox checkBox = (CheckBox) findViewById(R.id.enable_location);

        if(ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,PERMISSIONS_STRING,PERMISSIONS_REQUEST_CODE);
        }else{
            checkBox.setChecked(true);
        }
    }
}
