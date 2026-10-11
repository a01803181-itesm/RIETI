package itesm.rieti.view.inicioActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import itesm.rieti.viewModel.auth.AuthVM
import itesm.rieti.viewModel.auth.CognitoVM

/**
 * Pantalla para ingresar el código OTP (One Time Password) de registro.
 *
 * @param cognitoVM ViewModel de AWS Cognito.
 */
@Composable
fun OTPSignUpScreen(
    cognitoVM: CognitoVM
) {
    val authVM: AuthVM = viewModel()
    val authState by authVM.authState.collectAsState()
    val cognitoState by cognitoVM.cognitoState.collectAsState()

    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Ingresa tu código OTP",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Hemos enviado un código de 6 dígitos a ${authState.usuario?.correoU ?: ""}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        OTPCodeInput(
            code = cognitoState.otp,
            onCodeChange = {
                cognitoVM.setOTP(it)
                authVM.setError(null)
            },
            onCodeComplete = {
                cognitoVM.verifyOTP(
                    email = authState.usuario?.correoU ?: "",
                    onSuccess = { authVM.registerUser() },
                    onError = { authVM.setError(it) }
                )
            }
        )

        if (authState.error != null) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = authState.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/**
 * Pantalla para ingresar el código OTP de recuperación de contraseña y la nueva contraseña.
 *
 * @param authVM ViewModel de autenticación.
 * @param cognitoVM ViewModel de AWS Cognito.
 * @param modifier Modificador para la vista.
 */
@Composable
fun OTPRecoverPasswordScreen(
    authVM: AuthVM,
    cognitoVM: CognitoVM,
    modifier: Modifier = Modifier
) {
    val authState by authVM.authState.collectAsState()
    val cognitoState by cognitoVM.cognitoState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(24.dp)
    ) {
        TitulosLogin()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ingresa el código de 6 dígitos enviado a ${authState.usuario?.correoU ?: ""} y tu nueva contraseña.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))
        OTPCodeInput(
            code = cognitoState.otp,
            onCodeChange = {
                cognitoVM.setOTP(it)
                authVM.setError(null)
            },
            onCodeComplete = { }
        )
        Spacer(modifier = Modifier.height(24.dp))
        Contrasena(
            contrasena = cognitoState.password,
            contrasenaChange = {
                cognitoVM.setPassword(it)
                authVM.setError(null)
            }
        )
        if (authState.error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = authState.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                cognitoVM.confirmPasswordReset(
                    email = authState.usuario?.correoU ?: "",
                    newPassword = cognitoState.password,
                    confirmationCode = cognitoState.otp,
                    onSuccess = {
                        cognitoVM.setRecoveringPassword(false)
                        cognitoVM.resetState()
                        authVM.setError(null)
                    },
                    onError = { authVM.setError(it) }
                )
            },
            enabled = cognitoState.otp.length == 6 && cognitoState.password.isNotEmpty(),
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = "Cambiar contraseña")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = {
                cognitoVM.setRecoveringPassword(false)
                cognitoVM.resetState()
            }
        ) {
            Text(
                text = "Cancelar",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Componente para introducir un código OTP, compuesto por varias cajas de dígitos.
 *
 * @param code Código actual introducido.
 * @param onCodeChange Callback que se invoca cuando el código cambia.
 * @param onCodeComplete Callback que se invoca cuando el código alcanza la longitud requerida.
 * @param modifier Modificador para la vista.
 * @param otpLength Longitud total del código OTP (por defecto 6).
 */
@Composable
fun OTPCodeInput(
    code: String,
    onCodeChange: (String) -> Unit,
    onCodeComplete: (String) -> Unit,
    modifier: Modifier = Modifier,
    otpLength: Int = 6
) {
    BasicTextField(
        value = code,
        onValueChange = { code ->
            if (code.length <= otpLength && code.all { it.isDigit() }) {
                onCodeChange(code)
                if (code.length == otpLength) onCodeComplete(code)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(otpLength) { index ->
                    val char = code.getOrNull(index)?.toString() ?: ""
                    val isFocused = code.length == index

                    DigitBox(
                        char = char,
                        isFocused = isFocused
                    )
                }
            }
        }
    )
}

/**
 * Caja individual que muestra un único dígito del código OTP.
 *
 * @param char Carácter a mostrar.
 * @param isFocused Booleano que indica si la caja tiene el foco actual.
 */
@Composable
fun DigitBox(
    char: String,
    isFocused: Boolean
) {
    val borderColour = if (isFocused) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }

    Box(
        modifier = Modifier
            .size(52.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = borderColour,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Vista previa de la pantalla de ingreso del código OTP para el registro.
 *
 * @param cognitoVM ViewModel de AWS Cognito.
 */
@Preview(showBackground = true)
@Composable
fun OTPSignUpCodeInputPreview(cognitoVM: CognitoVM = CognitoVM()) {
    OTPSignUpScreen(cognitoVM = cognitoVM)
}