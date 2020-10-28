package com.example.androidappgestionbasura.casos_uso;

import android.app.Activity;
import android.content.Intent;

import com.example.androidappgestionbasura.model.Dispositivo;
import com.example.androidappgestionbasura.model.InterfaceDispositivos;
import com.example.androidappgestionbasura.model.TipoDispositivo;
import com.example.androidappgestionbasura.presentacion.DispositivoDetallesActivity;
import com.example.androidappgestionbasura.presentacion.FormularioCreacionBasura;

import static com.example.androidappgestionbasura.utility.Constantes.RESULT_RECYCLER_VIEW_BORRAR;
import static com.example.androidappgestionbasura.utility.Constantes.RESULT_RECYCLER_VIEW_EDITAR;

public class CasosUsoDispositivo {
    private Activity actividad;
    private InterfaceDispositivos dispositivos;
    public CasosUsoDispositivo(Activity actividad, InterfaceDispositivos dispositivos) {
        this.actividad = actividad;
        this.dispositivos = dispositivos;

    }
    // OPERACIONES BÁSICAS
    public void mostrar(int pos, int codigoRespuestaEdicionDispositivo) {
        Intent i = new Intent(actividad, DispositivoDetallesActivity.class);
        i.putExtra("pos", pos);
        actividad.startActivityForResult(i, codigoRespuestaEdicionDispositivo);
    }
    public void crear(TipoDispositivo tipo, int codigoRespuestaCreacionDispositivo){
        switch(tipo) {
            case BASURA:
                Intent Formulario = new Intent(actividad, FormularioCreacionBasura.class);
                actividad.startActivityForResult(Formulario, codigoRespuestaCreacionDispositivo);
                break;
            case ELECTRICO:  break;
            case AGUA:  break;
        }
    }

    /**
     * se llama cuando se da a aceptar en el dialog de confirmar
     * vuelve a mis dispositivos
     * @param id
     */
    public void borrar(int id) {
        dispositivos.borrar(id);
        Intent intent = new Intent();
        intent.putExtra("Dispositivo a borrar",id);
        actividad.setResult(RESULT_RECYCLER_VIEW_BORRAR,intent);
        actividad.finish();
    }

    /**
     * llama a formulario editar m¡dispositvos
     * @param pos
     */
    public void editar(int pos,int codigo) {
        Intent i = new Intent(actividad, FormularioCreacionBasura.class);
        i.putExtra("pos", pos);
        actividad.startActivityForResult(i, codigo );
    }
   public int add (Dispositivo dispositivo){
        dispositivos.añade(dispositivo);
        return dispositivos.tamaño()-1;

   }

    /**
     * actualiza el objeto de la array local
     * @param id
     * @param dispositivo
     */
    public void guardar(int id, Dispositivo dispositivo) {
        dispositivos.actualiza(id, dispositivo);
    }



}
