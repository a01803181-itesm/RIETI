package itesm.rieti.model.enums

import com.google.gson.annotations.SerializedName

enum class RangoEdad(val desc: String) {
    @SerializedName("INFANTES")
    INFANTES("Infantes (5 a 10 años)"),
    @SerializedName("PUBERTOS")
    PUBERTOS("Pubertos (11 a 13 años)"),
    @SerializedName("JOVENES")
    JOVENES("Jóvenes (14 a 17 años)"),
    @SerializedName("MIXTO")
    MIXTO("Mixto")
}