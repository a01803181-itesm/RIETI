package itesm.rieti.model.api
object GeneradorFolio {
    fun reporte(municipio: String, fecha: String): String {
        val munNames = municipio.split(" ")
        when (munNames.size) {
            1 -> return "RIETI-${munNames[0].substring(0,3).uppercase()}-${fecha}"
            2 -> return "RIETI-${munNames[0].substring(0,2).uppercase()}${munNames[1].substring(0,1)}-${fecha}"
            else -> return "RIETI${munNames[0].substring(0,1).uppercase()}${munNames[1].substring(0,1)}${munNames[2].substring(0,1)}-${fecha}"
        }
    }
}