package com.rahul.clearwalls.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rahul.clearwalls.domain.model.ImageQuality
import com.rahul.clearwalls.domain.model.Wallpaper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QualityPickerSheet(
    wallpaper: Wallpaper,
    onDismiss: () -> Unit,
    onQualitySelected: (ImageQuality) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Select Quality",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            ImageQuality.entries.forEach { quality ->
                val hasUrl = when (quality) {
                    ImageQuality.LOW -> wallpaper.lowUrl != null || wallpaper.previewUrl.isNotBlank()
                    ImageQuality.HD -> wallpaper.hdUrl != null || wallpaper.fullUrl.isNotBlank()
                    ImageQuality.TWO_K -> wallpaper.twoKUrl != null || wallpaper.fullUrl.isNotBlank()
                    ImageQuality.FOUR_K -> wallpaper.fourKUrl != null || wallpaper.fullUrl.isNotBlank()
                }
                if (hasUrl) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onQualitySelected(quality) }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Download,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = if (quality.isPremium) MaterialTheme.colorScheme.tertiary
                                else MaterialTheme.colorScheme.primary
                            )
                            Column {
                                Text(
                                    text = quality.displayName,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = quality.label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        if (quality.isPremium) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "Watch ad to unlock",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
