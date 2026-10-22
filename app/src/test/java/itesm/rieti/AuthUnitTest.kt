package itesm.rieti

import itesm.rieti.model.esquemas.Provider
import itesm.rieti.model.esquemas.Usuario
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.auth.AuthStage
import itesm.rieti.viewModel.auth.CognitoState
import itesm.rieti.viewModel.auth.CognitoVM
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthUnitTest {

    @Test
    fun `estado inicial de AuthState es por defecto no autenticado y con valores nulos`() {
        val state = AuthState()
        assertNull(state.usuario)
        assertNull(state.sub)
        assertNull(state.pictureURL)
        assertFalse(state.loggedIn)
        assertEquals(AuthStage.IDLE, state.stage)
        assertNull(state.error)
    }

    @Test
    fun `copia de AuthState actualiza correctamente todos los campos`() {
        val user = Usuario(correoU = "test@rieti.org", proveedor = Provider.COGNITO)
        val state = AuthState(
            usuario = user,
            sub = "sub-12345",
            pictureURL = "https://example.com/profile.jpg",
            loggedIn = true,
            stage = AuthStage.IDLE,
            error = null
        )

        assertEquals("test@rieti.org", state.usuario?.correoU)
        assertEquals(Provider.COGNITO, state.usuario?.proveedor)
        assertEquals("sub-12345", state.sub)
        assertEquals("https://example.com/profile.jpg", state.pictureURL)
        assertTrue(state.loggedIn)
        assertEquals(AuthStage.IDLE, state.stage)
        assertNull(state.error)
    }

    @Test
    fun `estado inicial de CognitoState es por defecto con campos vacios`() {
        val state = CognitoState()
        assertEquals("", state.password)
        assertEquals("", state.otp)
        assertFalse(state.otpSent)
        assertFalse(state.isRecoveringPassword)
    }

    @Test
    fun `mutadores de estado de CognitoVM actualizan el estado correctamente`() {
        val viewModel = CognitoVM()

        viewModel.setPassword("SecretPass123!")
        assertEquals("SecretPass123!", viewModel.cognitoState.value.password)

        viewModel.setOTP("123456")
        assertEquals("123456", viewModel.cognitoState.value.otp)

        viewModel.setRecoveringPassword(true)
        assertTrue(viewModel.cognitoState.value.isRecoveringPassword)

        viewModel.resetState()
        assertEquals("", viewModel.cognitoState.value.password)
        assertEquals("", viewModel.cognitoState.value.otp)
        assertFalse(viewModel.cognitoState.value.isRecoveringPassword)
    }
}
