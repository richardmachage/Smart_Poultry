package com.example.smartpoultry.presentation.composables.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.presentation.composables.spacers.MyHorizontalSpacer
import com.example.smartpoultry.presentation.composables.text.NormText

@Composable
fun MyOutlineButton(
    onButtonClick: () -> Unit,
    btnName: String,
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    leadingIcon : ImageVector? = null,
    trailingIcon: ImageVector? = null
){
    OutlinedButton(
        onClick = onButtonClick,
        modifier = modifier
            .padding(8.dp),
        enabled = enabled
    ) {
        leadingIcon?.let {
            Icon(imageVector = it, contentDescription = null)
            MyHorizontalSpacer(width = 3)
        }
        NormText(text = btnName)
        trailingIcon?.let {
            MyHorizontalSpacer(width = 3)
            Icon(imageVector = it, contentDescription = null)
        }
    }
}