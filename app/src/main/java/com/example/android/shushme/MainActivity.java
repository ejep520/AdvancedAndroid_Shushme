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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.android.shushme.databinding.ActivityMainBinding;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private ActivityMainBinding binding;

    private final static int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private final static int PLACE_PICKER_REQUEST = 1;

    /**
     * Called when the activity is starting
     *
     * @param savedInstanceState The Bundle that contains the data supplied in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set up the recycler view
        binding.placesListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PlaceListAdapter mAdapter = new PlaceListAdapter(this);
        binding.placesListRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int permissionStatus = ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            binding.locationPermissionCheckbox.setChecked(false);
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, permissionStatus);
            }
        } else {
            binding.locationPermissionCheckbox.setChecked(true);
            binding.locationPermissionCheckbox.setEnabled(false);
        }

        /*
        placesClient.findCurrentPlace(new FindCurrentPlaceRequest() {
            @NonNull
            @Override
            public List<Place.Field> getPlaceFields() {
                ArrayList<Place.Field> projection = new ArrayList<>();
                projection.add(Place.Field.ID);
                return projection;
            }

            @Nullable
            @Override
            public CancellationToken getCancellationToken() {
                return null;
            }
        }).addOnSuccessListener(findCurrentPlaceResponse -> Log.d("FindCurrentLoc",
                "Found you at " + findCurrentPlaceResponse.toString()))
        .addOnFailureListener(e -> {
            Log.d("FindCurrentLoc", "Could not find you.");
            e.printStackTrace();
        }); */
    }

    public void onAddPlaceButtonClick(View view) {
        if (ActivityCompat
                .checkSelfPermission(
                        MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)
        {
            Toast
                    .makeText(
                            MainActivity.this,
                            "You need to enable location permissions first.",
                            Toast.LENGTH_SHORT)
                    .show();
        } else {
            Intent intent = new Intent(this, NameAPlace.class);
            startActivityForResult(intent, 1);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1) && (resultCode == RESULT_OK)
    }
}
