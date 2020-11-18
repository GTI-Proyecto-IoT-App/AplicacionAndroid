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
    private static final String[] A = { "n/d", "preciso", "impreciso" };
    private static final String[] P = { "n/d", "bajo", "medio","alto" };
    private static final String[] E = { "fuera de servicio",
            "temporalmente no disponible ", "disponible" };

    private LocationManager manejador;
    private Location ultimaLocalizacion;
    private Marker mCurrLocationMarker;

    private String proveedor;
    private GoogleMap mapa;
    private MapView mapView;

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

        log("Proveedores de localización: \n ");
        muestraProveedores();
        Criteria criterio = new Criteria();
        criterio.setCostAllowed(false);
        criterio.setAltitudeRequired(false);
        criterio.setAccuracy(Criteria.ACCURACY_FINE);
        proveedor = manejador.getBestProvider(criterio, true);
        log("Mejor proveedor: " + proveedor + "\n");
        log("Comenzamos con la última localización conocida:");
        @SuppressLint("MissingPermission") Location localizacion = manejador.getLastKnownLocation(proveedor);
        ultimaLocalizacion = localizacion;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            manejador.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN,
                    this);
        }
        muestraLocaliz(ultimaLocalizacion);
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

        actualizarPosicion();

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
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
        log("Nueva localización: ");
        muestraLocaliz(location);
        ultimaLocalizacion = location;
        actualizarPosicion();
    }
    public void onProviderDisabled(String proveedor) {
        log("Proveedor deshabilitado: " + proveedor + "\n");
    }
    public void onProviderEnabled(String proveedor) {
        log("Proveedor habilitado: " + proveedor + "\n");
    }
    public void onStatusChanged(String proveedor, int estado, Bundle extras) {
        log("Cambia estado proveedor: " + proveedor + ", estado="
                + E[Math.max(0, estado)] + ", extras=" + extras + "\n");
    }
    // Métodos para mostrar información
    private void log(String cadena) {
        Log.d("TAG-PRUEBA",cadena);
    }
    private void muestraLocaliz(Location localizacion) {
        if (localizacion == null)
            log("Localización desconocida\n");
        else
            log(localizacion.toString() + "\n");
    }
    private void muestraProveedores() {
        log("Proveedores de localización: \n ");
        List<String> proveedores = manejador.getAllProviders();
        for (String proveedor : proveedores) {
            muestraProveedor(proveedor);
        }
    }

    private void muestraProveedor(String proveedor) {
        LocationProvider info = manejador.getProvider(proveedor);
        log("LocationProvider[ " + "getName=" + info.getName()
                + ", isProviderEnabled="
                + manejador.isProviderEnabled(proveedor) + ", getAccuracy="
                + A[Math.max(0, info.getAccuracy())] + ", getPowerRequirement="
                + P[Math.max(0, info.getPowerRequirement())]
                + ", hasMonetaryCost=" + info.hasMonetaryCost()
                + ", requiresCell=" + info.requiresCell()
                + ", requiresNetwork=" + info.requiresNetwork()
                + ", requiresSatellite=" + info.requiresSatellite()
                + ", supportsAltitude=" + info.supportsAltitude()
                + ", supportsBearing=" + info.supportsBearing()
                + ", supportsSpeed=" + info.supportsSpeed() + " ]\n");
    }

}