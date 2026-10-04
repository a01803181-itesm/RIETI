package itesm.rieti.model.esquemas

import com.google.gson.annotations.SerializedName

enum class Provider {
    @SerializedName("cognito")
    COGNITO,
    @SerializedName("google")
    GOOGLE
}
data class Usuario (
    val correoU: String,
    val proveedor: Provider
)
data class CheckEmail (
    val exists: Boolean,
    val provider: String?
)