import com.maxrave.simpmusic.ui.theme.typo
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import oblivionnocturne.composeapp.generated.resources.Res
import oblivionnocturne.composeapp.generated.resources.oblivion_owl

@Composable
fun BrandRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = Res.drawable.oblivion_owl,
            contentDescription = "Oblivion Nocturne Logo",
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(4.dp)),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Oblivion Nocturne",
            style = typo().bodyMedium,
        )
    }
}
