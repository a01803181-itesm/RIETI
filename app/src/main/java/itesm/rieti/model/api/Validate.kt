package itesm.rieti.model.api

import android.util.Log

/**
 * Enumeración que representa los posibles errores en los formularios.
 */
enum class FormError {
    InvalidName,
    ServerError,
    WorkTypeMissing,
    NNAMissing,
    DateTimeMissing,
    InvalidLocation,
    AgeRangeMissing,
}

/**
 * Objeto de validación de diferentes formatos y campos.
 */
object Validate {
    /**
     * Valida el formato del nombre completo, extrayendo el primer nombre y los apellidos.
     *
     * @param name Nombre completo ingresado por el usuario.
     * @param onSuccess Callback invocado al ser exitoso, entregando nombre, apellido paterno y apellido materno.
     * @param onError Callback invocado al fallar la validación, entregando el mensaje de error.
     */
    fun fullName(
        name: String,
        onSuccess: (String, String, String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.i("FullName", "Status: $name")
        if (name.isEmpty()) onSuccess("", "", "")
        val nameParts = name.split(" ")
        when (nameParts.size) {
            1 -> onError("Apellido paterno y materno es requerido")
            2 -> onError("Apellido materno es requerido")
            else -> onSuccess(nameParts[0], nameParts[nameParts.size - 2], nameParts[nameParts.size - 1])
        }
    }
}