package itesm.rieti.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import itesm.rieti.viewModel.api.UsuariosVM

@Composable
fun Borrador(usuariosVM: UsuariosVM = UsuariosVM())
{
    NuevoReporte(usuariosVM)
}

@Preview(showBackground = true)
@Composable
fun BorradorPreview()
{
    Borrador()
}