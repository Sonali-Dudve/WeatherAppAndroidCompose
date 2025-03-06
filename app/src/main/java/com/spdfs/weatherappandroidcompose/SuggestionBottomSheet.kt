import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spdfs.weatherappandroidcompose.R

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
            onDismissRequest = {  },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.Blue.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .wrapContentHeight(),
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
                confirmValueChange = { false }
            ),
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = tween(300))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                suggestions.forEachIndexed { index, suggestion ->
                    SuggestionItem(
                        text = suggestion,
                        onClick = {
                            onItemSelected(index)
                            onDismiss()
                        },
                        isLastItem = index == suggestions.size - 1,
                        index = index
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SuggestionItem(
    text: String,
    onClick: () -> Unit,
    isLastItem: Boolean,
    index: Int
) {
    val accentColor = when (index % 3) {
        0 -> MaterialTheme.colorScheme.primary
        1 -> MaterialTheme.colorScheme.secondary
        2 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(vertical = 7.dp, horizontal = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = colorResource(id = R.color.light_blue))
            .clickable(
                onClick = onClick,
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = 7.dp, horizontal = 6.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(accentColor, RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.White
                )
            )
        }
        if (!isLastItem) {
            Divider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                thickness = 0.5.dp,
                modifier = Modifier.padding(top = 14.dp)
            )
        }
    }
}