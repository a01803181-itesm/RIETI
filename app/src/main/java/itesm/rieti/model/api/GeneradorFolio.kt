package itesm.rieti.model.api

object GeneradorFolio {
    fun reporte(municipioCode: String, fecha: String) = "RIETI-$municipioCode-$fecha"
}