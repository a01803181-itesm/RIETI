package itesm.rieti.model.api
object GeneradorFolio {
    fun reporte(municipio: String, fecha: String): String {
        val munNames = municipio.split(" ")
        return when (munNames.size) {
            1 -> "RIETI-${munNames[0].substring(0,3).uppercase()}-${fecha}"
            2 -> "RIETI-${munNames[0].substring(0,2).uppercase()}${munNames[1].substring(0,1)}-${fecha}"
            else -> "RIETI-${munNames[0].substring(0,1).uppercase()}${munNames[1].substring(0,1)}${munNames[2].substring(0,1)}-${fecha}"
        }
    }
}