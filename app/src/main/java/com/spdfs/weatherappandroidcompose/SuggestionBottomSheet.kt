import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionBottomSheet(
    isVisible: Boolean,
    suggestions: List<String>,
    onDismiss: () -> Unit,
    onItemSelected: (Int) -> Unit
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .clip(MaterialTheme.shapes.medium)
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                suggestions.forEachIndexed { index, suggestion ->
                    Text(
                        text = suggestion,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clickable {
                                onItemSelected(index)
                                onDismiss()
                            }
                            .padding(12.dp)
                    )

                    if (index < suggestions.size - 1) {
                        Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)
                    }
                }
            }
        }
    }
}
