package itesm.rieti

import itesm.rieti.model.api.FormError
import itesm.rieti.model.drafts.Borrador
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.viewModel.nuevoReporte.DraftDeletionStage
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM
import itesm.rieti.viewModel.nuevoReporte.SaveState
import itesm.rieti.viewModel.nuevoReporte.SendState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NuevoReporteTest {

    private lateinit var viewModel: NuevoReporteVM

    @Before
    fun setUp() {
        viewModel = NuevoReporteVM()
    }

    @Test
    fun `setRawName updates state and clears InvalidName error`() {
        viewModel.setRawName("Alexander Mejia")
        assertEquals("Alexander Mejia", viewModel.state.value.rawName)
        assertFalse(viewModel.state.value.errors.containsKey(FormError.InvalidName))
    }

    @Test
    fun `assertFieldsCompletion correctly parses three-part full name`() {
        viewModel.setRawName("Giancarlo Moreno Vázquez")
        viewModel.setNNAs("2")
        viewModel.setRangoEdad(RangoEdad.INFANTES)
        viewModel.setWorkType(TipoTrabajo.VENTA_AMBULANTE)
        viewModel.setMunicipio("Monterrey")

        val isValid = viewModel.assertFieldsCompletion()

        assertTrue(isValid)
        assertEquals("Giancarlo", viewModel.state.value.reporte.nombre)
        assertEquals("Moreno", viewModel.state.value.reporte.apPaterno)
        assertEquals("Vázquez", viewModel.state.value.reporte.apMaterno)
    }

    @Test
    fun `assertFieldsCompletion correctly parses two-part full name`() {
        viewModel.setRawName("César Rodríguez")
        viewModel.setNNAs("1")
        viewModel.setRangoEdad(RangoEdad.PUBERTOS)
        viewModel.setWorkType(TipoTrabajo.MENDICIDAD)
        viewModel.setMunicipio("Guadalajara")

        val isValid = viewModel.assertFieldsCompletion()

        assertTrue(isValid)
        assertEquals("César", viewModel.state.value.reporte.nombre)
        assertEquals("Rodríguez", viewModel.state.value.reporte.apPaterno)
        assertEquals("", viewModel.state.value.reporte.apMaterno)
    }

    @Test
    fun `assertFieldsCompletion correctly parses single-part name`() {
        viewModel.setRawName("Santiago")
        viewModel.setNNAs("1")
        viewModel.setRangoEdad(RangoEdad.JOVENES)
        viewModel.setWorkType(TipoTrabajo.CAMPO)
        viewModel.setMunicipio("Puebla")

        val isValid = viewModel.assertFieldsCompletion()

        assertTrue(isValid)
        assertEquals("Santiago", viewModel.state.value.reporte.nombre)
        assertEquals("", viewModel.state.value.reporte.apPaterno)
        assertEquals("", viewModel.state.value.reporte.apMaterno)
    }

    @Test
    fun `setNNAs with valid integer updates numNinios and removes NNAMissing error`() {
        viewModel.setNNAs("3")
        assertEquals(3, viewModel.state.value.reporte.numNinios)
        assertFalse(viewModel.state.value.errors.containsKey(FormError.NNAMissing))
    }

    @Test
    fun `setNNAs with non-integer string adds NNAMissing error`() {
        viewModel.setNNAs("abc")
        assertTrue(viewModel.state.value.errors.containsKey(FormError.NNAMissing))
        assertEquals(
            "El campo debe ser un número entero positivo",
            viewModel.state.value.errors[FormError.NNAMissing]
        )
    }

    @Test
    fun `assertFieldsCompletion fails when numNinios is less than 1`() {
        viewModel.setNNAs("0")
        viewModel.setRangoEdad(RangoEdad.INFANTES)
        viewModel.setWorkType(TipoTrabajo.VENTA_AMBULANTE)
        viewModel.setMunicipio("Monterrey")

        val isValid = viewModel.assertFieldsCompletion()

        assertFalse(isValid)
        assertTrue(viewModel.state.value.errors.containsKey(FormError.NNAMissing))
        assertEquals(
            "Número de NNAs debe ser mayor a 0",
            viewModel.state.value.errors[FormError.NNAMissing]
        )
    }

    @Test
    fun `setRangoEdad updates state and clears AgeRangeMissing error`() {
        viewModel.setRangoEdad(RangoEdad.PUBERTOS)
        assertEquals(RangoEdad.PUBERTOS, viewModel.state.value.reporte.rangoEdad)
        assertFalse(viewModel.state.value.errors.containsKey(FormError.AgeRangeMissing))
    }

    @Test
    fun `assertFieldsCompletion fails when rangoEdad is missing`() {
        viewModel.setNNAs("2")
        viewModel.setWorkType(TipoTrabajo.VENTA_AMBULANTE)
        viewModel.setMunicipio("Monterrey")

        val isValid = viewModel.assertFieldsCompletion()

        assertFalse(isValid)
        assertTrue(viewModel.state.value.errors.containsKey(FormError.AgeRangeMissing))
        assertEquals(
            "Rango de edad no especificado",
            viewModel.state.value.errors[FormError.AgeRangeMissing]
        )
    }

    @Test
    fun `setHoraYFecha with valid ISO datetime updates dia and date`() {
        val isoDateTime = "2026-05-10T14:30:00"
        viewModel.setHoraYFecha(isoDateTime)

        assertEquals(isoDateTime, viewModel.state.value.reporte.dia)
        assertNotNull(viewModel.state.value.date)
        assertFalse(viewModel.state.value.errors.containsKey(FormError.DateTimeMissing))
    }

    @Test
    fun `assertFieldsCompletion sets default dia when blank`() {
        viewModel.setNNAs("1")
        viewModel.setRangoEdad(RangoEdad.MIXTO)
        viewModel.setWorkType(TipoTrabajo.LIMPIEZA_DE_PARABRISAS)
        viewModel.setMunicipio("Zapopan")

        val isValid = viewModel.assertFieldsCompletion()

        assertTrue(isValid)
        assertNotNull(viewModel.state.value.reporte.dia)
        assertTrue(viewModel.state.value.reporte.dia!!.isNotBlank())
    }

    @Test
    fun `setMunicipio updates state and removes InvalidLocation error`() {
        viewModel.setMunicipio("Monterrey")
        assertEquals("Monterrey", viewModel.state.value.reporte.municipio)
        assertFalse(viewModel.state.value.errors.containsKey(FormError.InvalidLocation))
    }

    @Test
    fun `setLocation updates direccion and removes InvalidLocation error`() {
        val address = "Av. Eugenio Garza Sada 2501"
        viewModel.setLocation(address)
        assertEquals(address, viewModel.state.value.reporte.direccion)
        assertFalse(viewModel.state.value.errors.containsKey(FormError.InvalidLocation))
    }

    @Test
    fun `assertFieldsCompletion fails when municipio is missing`() {
        viewModel.setNNAs("1")
        viewModel.setRangoEdad(RangoEdad.INFANTES)
        viewModel.setWorkType(TipoTrabajo.VENTA_AMBULANTE)

        val isValid = viewModel.assertFieldsCompletion()

        assertFalse(isValid)
        assertTrue(viewModel.state.value.errors.containsKey(FormError.InvalidLocation))
        assertEquals(
            "Municipio no especificado",
            viewModel.state.value.errors[FormError.InvalidLocation]
        )
    }

    @Test
    fun `setWorkType updates state and removes WorkTypeMissing error`() {
        viewModel.setWorkType(TipoTrabajo.CARGA_Y_DESCARGA)
        assertEquals(TipoTrabajo.CARGA_Y_DESCARGA, viewModel.state.value.reporte.tipoTrabajo)
        assertFalse(viewModel.state.value.errors.containsKey(FormError.WorkTypeMissing))
    }

    @Test
    fun `assertFieldsCompletion fails when tipoTrabajo is missing`() {
        viewModel.setNNAs("2")
        viewModel.setRangoEdad(RangoEdad.JOVENES)
        viewModel.setMunicipio("Monterrey")

        val isValid = viewModel.assertFieldsCompletion()

        assertFalse(isValid)
        assertTrue(viewModel.state.value.errors.containsKey(FormError.WorkTypeMissing))
        assertEquals(
            "Tipo de trabajo no especificado",
            viewModel.state.value.errors[FormError.WorkTypeMissing]
        )
    }

    @Test
    fun `setCoords updates latitud and longitud`() {
        viewModel.setCoords(25.65f, -100.29f)
        assertEquals(25.65f, viewModel.state.value.reporte.latitud)
        assertEquals(-100.29f, viewModel.state.value.reporte.longitud)
    }

    @Test
    fun `setDetails updates detallesAdicionales`() {
        val details = "NNA vendiendo dulces en el crucero vehicular"
        viewModel.setDetails(details)
        assertEquals(details, viewModel.state.value.reporte.detallesAdicionales)
    }

    @Test
    fun `setDisplayAnonymousDialog updates state`() {
        viewModel.setDisplayAnonymousDialog(true)
        assertTrue(viewModel.state.value.displayAnonymousDialog)
    }

    @Test
    fun `setSendState updates state`() {
        viewModel.setSendState(SendState.SENDING)
        assertEquals(SendState.SENDING, viewModel.state.value.sendState)
    }

    @Test
    fun `setSaveState updates state`() {
        viewModel.setSaveState(SaveState.SUCCESS)
        assertEquals(SaveState.SUCCESS, viewModel.state.value.saveState)
    }

    @Test
    fun `setSelectedBorrador updates state`() {
        val borrador = Borrador(idBorrador = 1, municipio = "Monterrey")
        viewModel.setSelectedBorrador(borrador)
        assertEquals(borrador, viewModel.state.value.selectedBorrador)
    }

    @Test
    fun `setDisplayConfirmDraftDeletion updates state`() {
        viewModel.setDisplayConfirmDraftDeletion(true)
        assertTrue(viewModel.state.value.displayConfirmDraftDeletion)
    }

    @Test
    fun `setDraftDeletionStage updates state`() {
        viewModel.setDraftDeletionStage(DraftDeletionStage.LOADING)
        assertEquals(DraftDeletionStage.LOADING, viewModel.state.value.draftDeletionStage)
    }

    @Test
    fun `assertFieldsCompletion fails on initial empty state and reports all missing fields`() {
        val isValid = viewModel.assertFieldsCompletion()

        assertFalse(isValid)
        val errors = viewModel.state.value.errors
        assertTrue(errors.containsKey(FormError.NNAMissing))
        assertTrue(errors.containsKey(FormError.WorkTypeMissing))
        assertTrue(errors.containsKey(FormError.AgeRangeMissing))
        assertTrue(errors.containsKey(FormError.InvalidLocation))
    }

    @Test
    fun `assertFieldsCompletion succeeds when all required fields are provided`() {
        viewModel.setRawName("Cassandra Padilla")
        viewModel.setNNAs("2")
        viewModel.setRangoEdad(RangoEdad.INFANTES)
        viewModel.setWorkType(TipoTrabajo.VENTA_AMBULANTE)
        viewModel.setMunicipio("Monterrey")
        viewModel.setLocation("Av. Hidalgo 100")
        viewModel.setCoords(25.68f, -100.31f)
        viewModel.setDetails("Detalles del incidente")

        val isValid = viewModel.assertFieldsCompletion()

        assertTrue(isValid)
        assertTrue(viewModel.state.value.errors.isEmpty())
        assertEquals("Cassandra", viewModel.state.value.reporte.nombre)
        assertEquals("Padilla", viewModel.state.value.reporte.apPaterno)
        assertEquals("", viewModel.state.value.reporte.apMaterno)
        assertEquals(2, viewModel.state.value.reporte.numNinios)
        assertEquals(RangoEdad.INFANTES, viewModel.state.value.reporte.rangoEdad)
        assertEquals(TipoTrabajo.VENTA_AMBULANTE, viewModel.state.value.reporte.tipoTrabajo)
        assertEquals("Monterrey", viewModel.state.value.reporte.municipio)
        assertEquals("Av. Hidalgo 100", viewModel.state.value.reporte.direccion)
    }

    @Test
    fun `fillFieldsWithBorrador correctly populates state from draft`() {
        val borrador = Borrador(
            idBorrador = 10,
            correoU = "user@test.com",
            rangoEdad = "INFANTES",
            tipoTrabajo = "VENTA_AMBULANTE",
            detallesAdicionales = "Borrador guardado",
            numNinios = 4,
            direccion = "Calle 5 de Mayo",
            municipio = "Monterrey",
            latitud = 25.6f,
            longitud = -100.3f,
            rawName = "Paul McCartney"
        )

        viewModel.fillFieldsWithBorrador(borrador)

        val state = viewModel.state.value
        assertEquals("Paul McCartney", state.rawName)
        assertEquals(4, state.reporte.numNinios)
        assertEquals(RangoEdad.INFANTES, state.reporte.rangoEdad)
        assertEquals(TipoTrabajo.VENTA_AMBULANTE, state.reporte.tipoTrabajo)
        assertEquals("Monterrey", state.reporte.municipio)
        assertEquals("Calle 5 de Mayo", state.reporte.direccion)
        assertEquals("Borrador guardado", state.reporte.detallesAdicionales)
        assertEquals(borrador, state.selectedBorrador)
        assertTrue(state.errors.isEmpty())
    }

    @Test
    fun `resetForm resets all fields to default state`() {
        viewModel.setRawName("Noel")
        viewModel.setNNAs("5")
        viewModel.setMunicipio("Guadalajara")

        viewModel.resetForm()

        val state = viewModel.state.value
        assertEquals("", state.rawName)
        assertEquals(0, state.reporte.numNinios)
        assertNull(state.reporte.municipio)
        assertTrue(state.errors.isEmpty())
    }
}
