package itesm.rieti.view.nuevoReporte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteState
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionarHorario(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var tempDate by remember { mutableStateOf<java.time.LocalDate>(Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneOffset.UTC).toLocalDate()) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    val timePickerState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
        is24Hour = false
    )

    val dateFormatted = try {
        if (nuevoReporteState.reporte.dia != null) {
            val parsed = LocalDateTime.parse(nuevoReporteState.reporte.dia)
            parsed.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a"))
        } else ""
    } catch (_: Exception) { "" }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.size(24.dp),
            shape = RectangleShape
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Seleccionar Fecha y Hora",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = buildAnnotatedString {
                append("Horario")
                withStyle(SpanStyle(color = Color.Red)) {
                    append(" *")
                }
            },
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = dateFormatted,
            style = MaterialTheme.typography.titleMedium
        )
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        tempDate = Instant.ofEpochMilli(selectedDate).atZone(ZoneOffset.UTC).toLocalDate()
                        showTimePicker = true
                    }
                }) {
                    Text("Siguiente")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                        val selectedTime =
                            LocalTime.of(timePickerState.hour, timePickerState.minute)
                        var datetime = LocalDateTime.of(tempDate, selectedTime)

                        val now = LocalDateTime.now()
                        if (datetime.isAfter(now)) {
                            datetime = now
                        }
                        nuevoReporteVM.setHoraYFecha(datetime.toString())
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Atrás")
                }
            },
            title = { Text("Seleccionar Hora") },
            text = { TimePicker(state = timePickerState) }
        )
    }
}