import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
        ModalBottomSheet(onDismissRequest = { onDismiss() }) {
            Column(Modifier.padding(16.dp)) {
                suggestions.forEachIndexed { index, suggestion ->
                    Text(
                        text = suggestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                onItemSelected(index)
                                onDismiss()
                            }
                    )
                }
            }
        }
    }
}
