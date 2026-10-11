package itesm.rieti.model.enums

import com.google.gson.annotations.SerializedName

/**
 * Enumeración que representa los distintos rangos de edad de un NNA.
 *
 * @property desc Descripción detallada del rango de edad para ser mostrada en la UI.
 */
enum class RangoEdad(val desc: String) {
    /** Rango para infantes entre 5 y 10 años. */
    @SerializedName("INFANTES")
    INFANTES("Infantes (5 a 10 años)"),
    /** Rango para pubertos entre 11 y 13 años. */
    @SerializedName("PUBERTOS")
    PUBERTOS("Pubertos (11 a 13 años)"),
    /** Rango para jóvenes entre 14 y 17 años. */
    @SerializedName("JOVENES")
    JOVENES("Jóvenes (14 a 17 años)"),
    /** Cuando se trata de un grupo mixto. */
    @SerializedName("MIXTO")
    MIXTO("Mixto")
}