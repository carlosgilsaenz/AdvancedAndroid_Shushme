import android.app.PendingIntent;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
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
                    setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT).
                    build();
            
            mGeofenceList.add(geofence);
        }
    }
}