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
        val munNames = municipio.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
        val prefix = when {
            munNames.isEmpty() -> "DES"
            munNames.size == 1 -> {
                val name = munNames[0].uppercase()
                if (name.length >= 3) name.substring(0, 3) else name.padEnd(3, 'X')
            }
            munNames.size == 2 -> {
                val p1 = if (munNames[0].length >= 2) munNames[0].substring(0, 2) else munNames[0].padEnd(2, 'X')
                val p2 = if (munNames[1].isNotEmpty()) munNames[1].substring(0, 1) else "X"
                (p1 + p2).uppercase()
            }
            else -> {
                val p1 = if (munNames[0].isNotEmpty()) munNames[0].substring(0, 1) else "X"
                val p2 = if (munNames[1].isNotEmpty()) munNames[1].substring(0, 1) else "X"
                val p3 = if (munNames[2].isNotEmpty()) munNames[2].substring(0, 1) else "X"
                (p1 + p2 + p3).uppercase()
            }
        }
        return "RIETI-$prefix-$fecha"
    }
}