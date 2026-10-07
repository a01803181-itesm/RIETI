package itesm.rieti.model.api

import itesm.rieti.model.enums.Municipio

object GeneradorFolio {
    fun reporte(municipio: Municipio, fecha: String) = "RIETI-${municipio.code}-$fecha"
}