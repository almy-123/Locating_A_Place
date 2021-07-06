package sg.edu.rp.id19037610.locatingaplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnNorth, btnCentral, btnEast;
    GoogleMap map;
    LatLng north888, centralTpy, eastTp, singapore;
    Spinner optSpinner;
    ArrayList<String> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        btnNorth = findViewById(R.id.btnNorth);
//        btnCentral = findViewById(R.id.btnCentral);
//        btnEast = findViewById(R.id.btnEast);
        optSpinner = findViewById(R.id.optSpinner);
        options = new ArrayList<String>();

        options.add("All Branches");
        options.add("My Location");
        options.add("North");
        options.add("Central");
        options.add("East");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optSpinner.setAdapter(spinnerAdapter);

        optSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (map != null) {
                    if (i == 0) {
                        // singapore
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(singapore, 11));
                    } else if (i == 1) {
                        // user location
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            Location location = map.getMyLocation();
                            double latitude = location.getLatitude();
                            double longtitute = location.getLongitude();

                            LatLng curr = new LatLng(latitude, longtitute);
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, 18));
                        } else {
                            Toast.makeText(MainActivity.this, "GPS access not granted", Toast.LENGTH_SHORT).show();
                        }
                    } else if (i == 2) {
                        // north
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(north888, 18));
                    } else if (i == 3) {
                        // central
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(centralTpy, 18));
                    } else if (i == 4) {
                        // east
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(eastTp, 18));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment =
                (SupportMapFragment) fm.findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                singapore = new LatLng(1.364917, 103.822872);

                UiSettings ui = map.getUiSettings();
                ui.setCompassEnabled(true);
                ui.setZoomControlsEnabled(true);

                north888 = new LatLng(1.4374301, 103.7953959);
                Marker north = map.addMarker(new
                        MarkerOptions()
                        .position(north888)
                        .title("HQ-North")
                        .snippet("888 Plaza, 888 Woodlands Drive 50, 730888")
                        .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_big_on)));

                centralTpy = new LatLng(1.3327, 103.8492);
                Marker central = map.addMarker(new
                        MarkerOptions()
                        .position(centralTpy)
                        .title("HQ-Central")
                        .snippet("Toa Payoh Mall, 185 Toa Payoh Central, 310185")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                eastTp = new LatLng(1.353610, 103.940380);
                Marker east = map.addMarker(new
                        MarkerOptions()
                        .position(eastTp)
                        .title("HQ-East")
                        .snippet("Our Tampines Hub, 1 Tampines Walk, 528523")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Toast.makeText(MainActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();

                        return false;
                    }
                });

                map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        optSpinner.setSelection(1);

                        return false;
                    }
                });

                int permissionCheck = PermissionChecker.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                } else {
                    Log.e("GMap - Permission", "GPS access has not been granted");
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
            }
        });

//        btnNorth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (map != null) {
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(north888, 18));
//                }
//            }
//        });
//
//        btnCentral.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (map != null) {
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(centralTpy, 18));
//                }
//            }
//        });
//
//        btnEast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (map != null) {
//                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(eastTp, 18));
//                }
//            }
//        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
    }
}