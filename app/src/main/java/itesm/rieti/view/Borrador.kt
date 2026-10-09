package itesm.rieti.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import itesm.rieti.view.nuevoReporte.NuevoReporte
import itesm.rieti.viewModel.auth.AuthState
import itesm.rieti.viewModel.nuevoReporte.UbicacionVM

@Composable
fun Borrador(modifier: Modifier = Modifier, authState: AuthState = AuthState(), ubicacionVM: UbicacionVM = UbicacionVM())
{
    NuevoReporte(authState, ubicacionVM)
}

@Preview(showBackground = true)
@Composable
fun BorradorPreview()
{
    Borrador()
}