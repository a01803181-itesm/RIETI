package itesm.rieti.model.api

import android.util.Log

enum class FormError(desc: String) {
    SurnameMissing("Apellido paterno y materno es requerido"),
    SurnameIncomplete("Apellido materno es requerido"),
    WorkTypeMissing("Tipo de trabajo es requerido"),
    DateTimeMissing("Horario es requerido"),
    ServerError("Error en el servidor")
}
object Validate {
    fun fullName(
        name: String,
        onSuccess: (String, String, String) -> Unit,
        onError: (FormError) -> Unit
    ) {
        Log.i("FullName", "Status: $name")
        if (name.isEmpty()) onSuccess("", "", "")
        val nameParts = name.split(" ")
        when (nameParts.size) {
            1 -> onError(FormError.SurnameMissing)
            2 -> onError(FormError.SurnameIncomplete)
            else -> onSuccess(nameParts[0], nameParts[nameParts.size - 2], nameParts[nameParts.size - 1])
        }
    }
}