package com.example.androidappgestionbasura.presentacion.HomeActivityPackage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.androidappgestionbasura.R;
import com.example.androidappgestionbasura.datos.firebase.callback.CallBack;
import com.example.androidappgestionbasura.model.GeoPunto;
import com.example.androidappgestionbasura.model.basura_municipal.BasuraMunicipal;
import com.example.androidappgestionbasura.model.basura_municipal.ListaBasurasMunicipales;
import com.example.androidappgestionbasura.repository.BasuraMunicipalRepository;
import com.example.androidappgestionbasura.repository.impl.BasuraMunicipalRepositoryRepositoyImpl;
import com.example.androidappgestionbasura.utility.Utility;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Mapa de las basuras municipales cerca
 * @author Ruben Pardo
 * Fecha: 16/11/2020
 */
public class BasuraMunicipalesMapsFragment extends Fragment implements OnMapReadyCallback, LocationListener {


    public static final int PERMISION_CODE_LOCATION = 1235;

    private static final long TIEMPO_MIN = 10 * 1000 ; // 10 segundos
    private static final long DISTANCIA_MIN = 5; // 5 metros

    private LocationManager manejador;
    private Location ultimaLocalizacion;

    private String proveedor;
    private GoogleMap mapa;
    private MapView mapView;

    private ListaBasurasMunicipales listaBasurasMunicipales;
    private BasuraMunicipalRepository basuraMunicipalRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_basura_municipales_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        basuraMunicipalRepository = new BasuraMunicipalRepositoryRepositoyImpl();
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        // pedir permisos de localizacion
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            initLocation();
        }else{
            Utility.permissionAsk(Manifest.permission.ACCESS_FINE_LOCATION,getActivity(),PERMISION_CODE_LOCATION);
        }


    }

    private void initLocation() {
        mapView.getMapAsync(this);
        manejador = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor = manejador.getBestProvider(criterio, true);
        @SuppressLint("MissingPermission") Location localizacion = manejador.getLastKnownLocation(proveedor);
        ultimaLocalizacion = localizacion;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            manejador.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN,
                    this);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISION_CODE_LOCATION) {
            if (grantResults.length== 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                initLocation();

            } else {
                Toast.makeText(getActivity(), "Sin el permiso, no puedo realizar la " +
                        "acción", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        basuraMunicipalRepository.readBasurasMunicipales(new CallBack() {
            @Override
            public void onSuccess(Object object) {
                listaBasurasMunicipales = (ListaBasurasMunicipales) object;
                mostrarBasurasEnMapa();
            }

            @Override
            public void onError(Object object) {
                // TODO MOSTRAR ERROR
            }
        });




        actualizarPosicion();

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
        }
    }

    private void mostrarBasurasEnMapa() {

        for(BasuraMunicipal basuraMunicipal: listaBasurasMunicipales.getBasuraMunicipalList()){
            LatLng location = new LatLng(basuraMunicipal.getPosicion().getLat(),basuraMunicipal.getPosicion().getLng());
            mapa.addMarker(new MarkerOptions().position(location).icon(Utility.bitmapDescriptorFromVector(getActivity(),basuraMunicipal.getTipo().getRecurso())));
        }


    }

    private void actualizarPosicion() {
        LatLng current = new LatLng(ultimaLocalizacion.getLatitude(), ultimaLocalizacion.getLongitude());
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(current,15.0F));
    }

    // Métodos del ciclo de vida de la actividad
    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        if(manejador !=null){
            manejador.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN,
                    this);
        }
    }
    @Override public void onPause() {
        super.onPause();
        if(manejador!=null){
            manejador.removeUpdates(this);
        }
    }
    // Métodos de la interfaz LocationListener
    public void onLocationChanged(Location location) {
        ultimaLocalizacion = location;
        actualizarPosicion();
    }






}