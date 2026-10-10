package itesm.rieti.model.enums

import com.google.gson.annotations.SerializedName

enum class TipoTrabajo(val desc: String) {
    @SerializedName("VENTA_AMBULANTE")
    VENTA_AMBULANTE("Venta ambulante"),
    @SerializedName("LIMPIEZA_DE_PARABRISAS")
    LIMPIEZA_DE_PARABRISAS("Limpieza de Parabrisas"),
    @SerializedName("MENDICIDAD")
    MENDICIDAD("Mendicidad"),
    @SerializedName("CARGA_Y_DESCARGA")
    CARGA_Y_DESCARGA("Carga y descarga"),
    @SerializedName("TRABAJO_EN_COMERCIO")
    TRABAJO_EN_COMERCIO("Trabajo en comercio"),
    @SerializedName("CAMPO")
    CAMPO("Campo"),
    @SerializedName("CONSTRUCCION")
    CONSTRUCCION("construcción"),
    @SerializedName("TRABAJO_DOMESTICO")
    TRABAJO_DOMESTICO("trabajo doméstico"),
    @SerializedName("RECOLECCION_DE_RESIDUOS")
    RECOLECCION_DE_RESIDUOS("recolección de residuos"),
    @SerializedName("OTRA_ACTIVIDAD")
    OTRA_ACTIVIDAD("otra actividad"),
}