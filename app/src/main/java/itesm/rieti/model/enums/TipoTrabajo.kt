package itesm.rieti.model.enums

import com.google.gson.annotations.SerializedName

/**
 * Enumeración de los diferentes tipos de trabajo en los que pueden verse involucrados los NNA.
 *
 * @property desc Descripción del trabajo para ser mostrada en la interfaz gráfica.
 */
enum class TipoTrabajo(val desc: String) {
    /** Venta ambulante de productos en la calle. */
    @SerializedName("VENTA_AMBULANTE")
    VENTA_AMBULANTE("Venta ambulante"),
    /** Limpieza de parabrisas en cruceros o semáforos. */
    @SerializedName("LIMPIEZA_DE_PARABRISAS")
    LIMPIEZA_DE_PARABRISAS("Limpieza de Parabrisas"),
    /** Situación de mendicidad (pedir dinero). */
    @SerializedName("MENDICIDAD")
    MENDICIDAD("Mendicidad"),
    /** Labores de carga y descarga de materiales o productos. */
    @SerializedName("CARGA_Y_DESCARGA")
    CARGA_Y_DESCARGA("Carga y descarga"),
    /** Trabajo de ayuda en comercios establecidos. */
    @SerializedName("TRABAJO_EN_COMERCIO")
    TRABAJO_EN_COMERCIO("Trabajo en comercio"),
    /** Labores y trabajos en el campo o agricultura. */
    @SerializedName("CAMPO")
    CAMPO("Campo"),
    /** Trabajo en obras de construcción. */
    @SerializedName("CONSTRUCCION")
    CONSTRUCCION("construcción"),
    /** Labores de trabajo doméstico. */
    @SerializedName("TRABAJO_DOMESTICO")
    TRABAJO_DOMESTICO("trabajo doméstico"),
    /** Labores de recolección de residuos o reciclaje. */
    @SerializedName("RECOLECCION_DE_RESIDUOS")
    RECOLECCION_DE_RESIDUOS("recolección de residuos"),
    /** Cualquier otra actividad no descrita anteriormente. */
    @SerializedName("OTRA_ACTIVIDAD")
    OTRA_ACTIVIDAD("otra actividad"),
}