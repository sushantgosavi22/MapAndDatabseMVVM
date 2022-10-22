package com.sushant.mapanddatabasemvvm.ui.map

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sushant.mapanddatabasemvvm.R
import com.sushant.mapanddatabasemvvm.database.model.UiLocationEntry
import com.sushant.mapanddatabasemvvm.databinding.ActivityMapsBinding
import com.sushant.mapanddatabasemvvm.ui.locationEntry.LocationAddMarkerFragment
import com.sushant.mapanddatabasemvvm.ui.locationEntry.LocationAddMarkerFragment.Companion.BUNDLE_KEY_ENTRY
import com.sushant.mapanddatabasemvvm.ui.locationEntry.LocationAddMarkerViewModel


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var locationAddMarkerViewModel: LocationAddMarkerViewModel
    private var mMap: GoogleMap? = null
    private lateinit var binding: ActivityMapsBinding
    private var cameraPosition: CameraPosition? = null
    private var lastKnownList: ArrayList<UiLocationEntry>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationAddMarkerViewModel = ViewModelProvider(
            this, LocationAddMarkerViewModel
                .ViewModelFactory(application)
        )[LocationAddMarkerViewModel::class.java]

        initUi()
    }

    private fun initUi() {
        supportFragmentManager.setFragmentResultListener(BUNDLE_KEY_ENTRY, this) { _, _ ->
            //Refresh list when new marker added
            locationAddMarkerViewModel.fetchEntryList()
        }

        binding.fabAdd.setOnClickListener {
            cameraPosition?.target?.let { latLng ->
                val bottomSheet = LocationAddMarkerFragment.newInstance(latLng)
                bottomSheet.show(
                    supportFragmentManager,
                    LocationAddMarkerFragment::class.simpleName
                )
            }
        }

        locationAddMarkerViewModel.getEntryList().observe(this) { list ->
            lastKnownList = list
            addMarkers()
        }

        /**
         * Below code check if there is data available in persisted state.
         * In the case of activity recreated eg. screen rotation, config changes
         * Then restore the persisted data [remove extra api/database call ]
         */
        if (locationAddMarkerViewModel.isPersistedAvailable().value == false) {
            locationAddMarkerViewModel.fetchEntryList()
            locationAddMarkerViewModel.setPersisted(true)
        } else {
            locationAddMarkerViewModel.getEntryList().value?.let { list ->
                lastKnownList = list
            }
        }
    }

    private fun addMarkers() {
        lastKnownList?.let { list ->
            mMap?.clear()
            if (list.isNotEmpty()) {
                list.map { model ->
                    model.latLng?.let { latLng ->
                        mMap?.addMarker(
                            MarkerOptions().position(latLng).title(model.propertyName)
                        )
                    }
                }
                list.last().latLng?.let {
                    mMap?.moveCamera(CameraUpdateFactory.newLatLng(it))
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // We can provide input as GoogleMap.MAP_TYPE_SATELLITE but
        // In that case we are unable to see location name so that why use MAP_TYPE_HYBRID\
        mMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
        mMap?.setOnCameraIdleListener {
            // fetch latest center position
            cameraPosition = mMap?.cameraPosition
        }
        addMarkers()
        // Starting camera location added default Location is Sydney and move the camera
        val lastKnown = lastKnownList?.last()?.latLng ?: LatLng(-34.0, 151.0)
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(lastKnown))
    }
}