package itesm.rieti.view.nuevoReporteActivity

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import itesm.rieti.R
import itesm.rieti.model.api.FormError
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteState
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM

/**
 * Componente abstracto contenedor para los campos de formulario.
 * Envuelve el contenido en una [Surface] que maneja la pérdida de foco e interacción
 * al tocar fuera del campo, desdibujando bordes activos y ocultando el teclado.
 *
 * @param modifier Modificador para la vista.
 * @param shape Forma de la superficie.
 * @param color Color de fondo de la superficie.
 * @param content Contenido composable del campo de formulario.
 */
@Composable
fun FormField(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    color: Color = Color.Transparent,
    content: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Surface(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },
        shape = shape,
        color = color
    ) {
        content()
    }
}

/**
 * Campo para ingresar o ajustar la cantidad de Niños, Niñas o Adolescentes (NNA).
 *
 * @param nuevoReporteVM ViewModel del nuevo reporte.
 * @param nuevoReporteState Estado del nuevo reporte.
 * @param modifier Modificador para la vista.
 */
@Composable
fun CantidadNNA(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    FormField(modifier = modifier) {
        OutlinedTextField(
            value = nuevoReporteState.reporte.numNinios.toString(),
            textStyle = TextStyle(textAlign = TextAlign.Center),
            onValueChange = { nuevoReporteVM.setNNAs(it) },
            label = {
                Text(
                    text = buildAnnotatedString {
                        append("No. de NNAs")
                        withStyle(SpanStyle(color = Color.Red)) {
                            append(" *")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Left,
                )
            },
            leadingIcon = {
                IconButton(
                    onClick = { if (nuevoReporteState.reporte.numNinios > 1) nuevoReporteVM.setNNAs((nuevoReporteState.reporte.numNinios - 1).toString()) },
                    enabled = nuevoReporteState.reporte.numNinios > 1,
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Restar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(4.dp)
                ) {
                    IconButton(
                        onClick = { nuevoReporteVM.setNNAs((nuevoReporteState.reporte.numNinios + 1).toString()) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Sumar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            isError = nuevoReporteState.errors.contains(FormError.NNAMissing),
            supportingText = {
                if (nuevoReporteState.errors.contains(FormError.NNAMissing)) {
                    Text(
                        text = nuevoReporteState.errors[FormError.NNAMissing] ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Menú desplegable para seleccionar el rango de edad.
 *
 * @param nuevoReporteVM ViewModel del nuevo reporte.
 * @param nuevoReporteState Estado del nuevo reporte.
 * @param modifier Modificador para la vista.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Edad(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    FormField(modifier = modifier) {
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                readOnly = true,
                value = nuevoReporteState.reporte.rangoEdad?.desc ?: "",
                label = {
                    Text(
                        text = buildAnnotatedString {
                            append("Rango edad")
                            withStyle(SpanStyle(color = Color.Red)) {
                                append(" *")
                            }
                        },
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                onValueChange = { },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                isError = nuevoReporteState.errors.contains(FormError.AgeRangeMissing),
                supportingText = {
                    if (nuevoReporteState.errors.contains(FormError.AgeRangeMissing)) {
                        Text(
                            text = nuevoReporteState.errors[FormError.AgeRangeMissing] ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                RangoEdad.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.desc) },
                        onClick = {
                            nuevoReporteVM.setRangoEdad(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * Menú desplegable para seleccionar el tipo de trabajo reportado.
 *
 * @param nuevoReporteVM ViewModel del nuevo reporte.
 * @param nuevoReporteState Estado del nuevo reporte.
 * @param modifier Modificador para la vista.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipoTrabajoDropdown(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    FormField(modifier = modifier) {
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                readOnly = true,
                value = nuevoReporteState.reporte.tipoTrabajo?.desc ?: "",
                label = {
                    Text(
                        text = buildAnnotatedString {
                            append("Tipo de trabajo")
                            withStyle(SpanStyle(color = Color.Red)) {
                                append(" *")
                            }
                        },
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                onValueChange = { },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                isError = nuevoReporteState.errors.contains(FormError.WorkTypeMissing),
                supportingText = {
                    if (nuevoReporteState.errors.contains(FormError.WorkTypeMissing)) {
                        Text(
                            text = nuevoReporteState.errors[FormError.WorkTypeMissing] ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                TipoTrabajo.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.desc) },
                        onClick = {
                            nuevoReporteVM.setWorkType(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * Campo de texto para ingresar detalles adicionales sobre el reporte.
 *
 * @param nuevoReporteVM ViewModel del nuevo reporte.
 * @param nuevoReporteState Estado del nuevo reporte.
 * @param modifier Modificador para la vista.
 */
@Composable
fun Detalles(
    nuevoReporteVM: NuevoReporteVM,
    nuevoReporteState: NuevoReporteState,
    modifier: Modifier = Modifier
) {
    var showDetallesHelpDialog by remember { mutableStateOf(false) }

    if (showDetallesHelpDialog) {
        DetallesHelpDialog(onDismiss = { showDetallesHelpDialog = false })
    }

    FormField(modifier = modifier) {
        OutlinedTextField(
            value = nuevoReporteState.reporte.detallesAdicionales ?: "",
            onValueChange = { nuevoReporteVM.setDetails(it) },
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Observaciones",
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(
                        onClick = { showDetallesHelpDialog = true }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.info),
                            contentDescription = "Información",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )
    }
}

/**
 * Campo de texto para ingresar el nombre completo del reportante o dejarlo en blanco (Anónimo).
 *
 * @param nuevoReporteVM ViewModel del nuevo reporte.
 * @param nuevoReporteState Estado del nuevo reporte.
 * @param modifier Modificador para la vista.
 */
@Composable
fun NombreCompleto(nuevoReporteVM: NuevoReporteVM, nuevoReporteState: NuevoReporteState, modifier: Modifier = Modifier)
{
    FormField(modifier = modifier) {
        var showHelpDialog by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = nuevoReporteState.rawName,
            onValueChange = { nuevoReporteVM.setRawName(it) },
            label = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nombre Completo (o Anónimo)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(onClick = { showHelpDialog = true }) {
                        Icon(
                            painter = painterResource(R.drawable.info),
                            contentDescription = "Ayuda",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            isError = nuevoReporteState.errors.contains(FormError.InvalidName),
            shape = RoundedCornerShape(12.dp),
            supportingText = {
                if (nuevoReporteState.errors.contains(FormError.InvalidName)) {
                    Text(
                        text = nuevoReporteState.errors[FormError.InvalidName] ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
        if (showHelpDialog) {
            NombreCompletoHelpDialog { showHelpDialog = false }
        }
    }
}