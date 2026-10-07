package itesm.rieti.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import itesm.rieti.viewModel.nuevoReporte.NuevoReporteVM

@Composable
fun Borrador(modifier: Modifier = Modifier)
{
    NuevoReporte()
}

@Preview(showBackground = true)
@Composable
fun BorradorPreview()
{
    Borrador()
}