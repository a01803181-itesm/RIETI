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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import itesm.rieti.model.cognito.SignUp.confirmUserAccount
import itesm.rieti.viewModel.LogInVM

@Composable
fun OTPScreen(
    loginVM: LogInVM,
    email: String,
    onSuccess: () -> Unit
) {
    val otp by loginVM.otp.collectAsState()
    val errorMessage by loginVM.error.collectAsState()

    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Ingresa tu código OTP",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Hemos enviado un código de 6 dígitos a $email",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        OTPCodeInput(
            code = otp,
            onCodeChange = {
                loginVM.updateOtp(it)
                loginVM.updateError(null)
            },
            onCodeComplete = {
                confirmUserAccount(
                    email = email,
                    confirmationCode = otp,
                    onConfirmed = { onSuccess() },
                    onError = { _ -> loginVM.updateError("Código incorrecto. Inténtalo de nuevo.")}
                )
            }
        )

        if (errorMessage != null) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

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
        onValueChange = {
            if (it.length <= otpLength && it.all { it.isDigit() }) {
                onCodeChange(it)
                if (it.length == otpLength) onCodeComplete(it)
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

@Preview(showBackground = true)
@Composable
fun OTPCodeInputPreview(loginVM: LogInVM = LogInVM()) {
    OTPScreen(
        loginVM = loginVM,
        email = "william.henry.harrison@example-pet-store.com",
        onSuccess = {}
    )
}