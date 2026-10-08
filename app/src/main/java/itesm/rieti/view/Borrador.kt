package itesm.rieti.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import itesm.rieti.view.nuevoReporte.NuevoReporte
import itesm.rieti.viewModel.nuevoReporte.UbicacionVM

@Composable
fun Borrador(ubicacionVM: UbicacionVM = UbicacionVM(), modifier: Modifier = Modifier)
{
    NuevoReporte(ubicacionVM)
}

@Preview(showBackground = true)
@Composable
fun BorradorPreview()
{
    Borrador()
}