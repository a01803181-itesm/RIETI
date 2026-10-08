package itesm.rieti.model.api

import android.util.Log

enum class FormError {
    InvalidName,
    ServerError,
    WorkTypeMissing,
    NNAMissing,
    DateTimeMissing,
    InvalidLocation,
    AgeRangeMissing,
}
object Validate {
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