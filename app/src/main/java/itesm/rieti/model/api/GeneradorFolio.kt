package itesm.rieti.model.api

/**
 * Objeto encargado de generar folios para los reportes.
 */
object GeneradorFolio {
    /**
     * Genera un folio único para un reporte basado en el municipio y la fecha.
     *
     * @param municipio Nombre del municipio.
     * @param fecha Fecha de generación del reporte.
     * @return El folio generado como una cadena de texto.
     */
    fun reporte(municipio: String, fecha: String): String {
        val munNames = municipio.split(" ")
        return when (munNames.size) {
            1 -> "RIETI-${munNames[0].substring(0,3).uppercase()}-${fecha}"
            2 -> "RIETI-${munNames[0].substring(0,2).uppercase()}${munNames[1].substring(0,1)}-${fecha}"
            else -> "RIETI-${munNames[0].substring(0,1).uppercase()}${munNames[1].substring(0,1)}${munNames[2].substring(0,1)}-${fecha}"
        }
    }
}