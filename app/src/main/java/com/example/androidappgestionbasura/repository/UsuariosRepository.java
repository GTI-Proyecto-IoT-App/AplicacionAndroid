package com.example.androidappgestionbasura.repository;

import com.example.androidappgestionbasura.callback.CallBack;
import com.example.androidappgestionbasura.model.Usuario;

import java.util.HashMap;


/**
 * @author Ruben Pardo Casanova
 * creamos una interfaz donde agregamos
 * todas las funciones abstractas que son necesarias
 * para la manipulación de datos de la colección de usuarios.
 */
public interface UsuariosRepository {

    void createUsuario(Usuario user, CallBack callback);
    void updateUsuario(String userId, HashMap map, CallBack callback);
    void deleteUsuario(String userId, CallBack callback);
    void readUsuarioByKey(String userId, CallBack callback);

}
