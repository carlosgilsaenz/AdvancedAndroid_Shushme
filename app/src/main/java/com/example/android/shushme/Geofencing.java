package com.example.android.shushme;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by csaenz on 9/21/2017.
 */

public class Geofencing {

    // Member variables
    private static final int DAYS_FOR_GEOFENCE = 1;
    private static final float GEOFENCE_RADIUS_IN_METERS = 5;
    private static final String TAG = Geofencing.class.getName();

    private GoogleApiClient mGoogleClient;
    private Context mContext;
    private List<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;

    public Geofencing(GoogleApiClient client, Context context) {
        mContext = context;
        mGoogleClient = client;
        mGeofencePendingIntent = null;
        mGeofenceList = new ArrayList<>();
    }

    public void updateGeofencesList(PlaceBuffer places) {

        if (places.getCount() == 0) return;

        for (Place place : places) {
            Geofence geofence = new Geofence.Builder().
                    setRequestId(place.getId()).
                    setExpirationDuration(TimeUnit.DAYS.toMillis(DAYS_FOR_GEOFENCE)).
                    setCircularRegion(place.getLatLng().latitude,place.getLatLng().longitude,
                            GEOFENCE_RADIUS_IN_METERS).
                    setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT).
                    build();
            
            mGeofenceList.add(geofence);
        }
    }

    public GeofencingRequest getGeofencingRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    public PendingIntent getGeofencePendingIntent(){
        if(mGeofencePendingIntent != null){return mGeofencePendingIntent;}

        Intent intent = new Intent(mContext,Geofencing.class);
        mGeofencePendingIntent = PendingIntent.getBroadcast(mContext,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }
}