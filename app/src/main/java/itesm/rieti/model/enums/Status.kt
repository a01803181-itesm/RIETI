package itesm.rieti.model.enums

/**
 * Enumeración que representa el estado actual de un expediente.
 */
enum class Status {
    /** Expediente recién registrado. */
    REGISTRADO, 
    /** Expediente en proceso de revisión. */
    EN_REVISION, 
    /** Expediente con seguimiento activo. */
    EN_SEGUIMIENTO, 
    /** Expediente canalizado a las instituciones correspondientes. */
    CANALIZADO,
    /** Expediente cuyo proceso ha finalizado con éxito. */
    CONCLUIDO, 
    /** Expediente almacenado y archivado. */
    ARCHIVADO, 
    /** Expediente cancelado por algún motivo válido. */
    CANCELADO, 
    /** Expediente de un NNA reincidente. */
    REINCIDENTE
}