package com.example.android.shushme;

import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.example.android.shushme.provider.PlaceContract.PlaceEntry;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

public class GeofenceBroadcastReciever {
    private final Context mContext;

    public GeofenceBroadcastReciever(Context context) {
        mContext = context;
    }

    @Nullable
    private Geofence populateGeofence() {
        Cursor allPlaces = mContext
                .getContentResolver()
                .query(PlaceEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
        if (!allPlaces.moveToFirst()) {
            allPlaces.close();
            return null;
        }
        Geofence.Builder builder = new Geofence.Builder();
        final int idColumnIndex = allPlaces.getColumnIndex(PlaceEntry.COLUMN_PLACE_ID);
        for (int counter = 0; counter < allPlaces.getCount(); counter++) {
            builder.setRequestId(allPlaces.getString(idColumnIndex))
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);
            allPlaces.moveToNext();
        }
        Geofence fence = builder.build();
        allPlaces.close();
        return fence;
    }

    @Nullable
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        Geofence fence = populateGeofence();
        if (fence == null) {
            return null;
        }
        builder.addGeofence(fence);
        return builder.build();
    }
}
