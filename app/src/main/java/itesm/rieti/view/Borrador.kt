package itesm.rieti.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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