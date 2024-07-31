package com.example.smartpoultry.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.R


@Composable
fun NormText(
    modifier: Modifier = Modifier,
    text: String) {
    Text(
        modifier = modifier.padding(4.dp),
        text = text,
        maxLines = 1,
    )
}

@Composable
fun NormButton(
    onButtonClick: () -> Unit,
    btnName: String,
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    leadingIcon : ImageVector? = null,
    trailingIcon: ImageVector? = null
) {
    Button(
        onClick = onButtonClick,
        modifier = modifier
            //.fillMaxWidth()
            .padding(8.dp),
        enabled = enabled
    ) {
        leadingIcon?.let {
            Icon(imageVector = it, contentDescription = null)
        }
        MyHorizontalSpacer(width = 3)
        NormText(text = btnName)
        MyHorizontalSpacer(width = 3)
        trailingIcon?.let {
            Icon(imageVector = it, contentDescription = null)
        }    }
}

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
        }
        MyHorizontalSpacer(width = 3)
        NormText(text = btnName)
        MyHorizontalSpacer(width = 3)
        trailingIcon?.let {
            Icon(imageVector = it, contentDescription = null)
        }

    }
}
@Composable
fun MyOutlineTextFiled(
    modifier: Modifier = Modifier,
    label: String,
    keyboardType: KeyboardType,
    maxLines: Int = 1,
    initialText: String,
    onValueChange: (String) -> Unit,
) {
    var text by remember {
        mutableStateOf(initialText)
    }
    OutlinedTextField(
        modifier = modifier,
        label = { Text(text = label) },
        value = text,
        onValueChange = {
            text = it
            onValueChange(text)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        maxLines = maxLines,
    )
}

@Composable
fun MyTextButton(onButtonClick: () -> Unit, btnText: String, modifier: Modifier = Modifier) {
    TextButton(onClick = onButtonClick) {
        Text(
            text = btnText,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MyEditText(
    value : String ,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    iconLeading: ImageVector,
    iconLeadingDescription: String,
    readOnly: Boolean = false,
    enabled: Boolean = true
    ) {
    var text by remember { mutableStateOf(value) }
    OutlinedTextField(
        modifier = Modifier
           // .fillMaxWidth()
            //.padding(start = 8.dp, end = 8.dp)
                ,
        value = text,
        onValueChange = { newText -> text = newText },
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = {
            Icon(
                imageVector = iconLeading,
                contentDescription = iconLeadingDescription
            )
        },
        singleLine = true,
        readOnly = readOnly,
        enabled = enabled


    )
}

@Composable
fun MySimpleEditText(
    keyboardType: KeyboardType,
    iconLeading: ImageVector,
    iconLeadingDescription: String,
    modifier: Modifier
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        modifier = modifier
            .fillMaxSize(),
        value = text,
        onValueChange = { newText -> text = newText },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = {
            Icon(
                imageVector = iconLeading,
                contentDescription = iconLeadingDescription
            )
        },
        singleLine = true,
    )
}

@Composable
fun MyEditTextClear(
    value: String = "",
    label: String = "",
    hint: String = "",
    iconLeading: ImageVector,
    iconLeadingDescription: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit = {},
    hasError : Boolean = false,
    singleLine: Boolean = true,
    onClear : () ->Unit = {},
    prefix :  @Composable() (() -> Unit)? = null,
    supportingText :  @Composable (() -> Unit)? = null,
) {
    var text by remember { mutableStateOf(TextFieldValue(value)) }
    val color by animateColorAsState(
        targetValue = if (hasError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "color",
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (8.dp), end = (8.dp)),
        value = text,
        label = { Text(text = label) },
        placeholder = { Text(text = hint) },
        onValueChange = { newText ->
            text = newText
            onValueChange(newText.text)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = {
            Row(
                modifier = Modifier.padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
            Icon(
                modifier = Modifier.padding(5.dp),
                imageVector = iconLeading,
                contentDescription = iconLeadingDescription
            )
                
                if (prefix != null) {
                    MyHorizontalSpacer(width = 5)
                    prefix()
                }
            }
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = text.toString().isNotEmpty(),
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                IconButton(onClick = {
                    onClear()
                    text = TextFieldValue("")
                }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "clear text",
                        modifier = Modifier.size(24.dp),
                        tint = color
                    )
                }
            }

           /* IconButton(onClick = { text = TextFieldValue("") }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear_input")
            }*/
        },
        singleLine = singleLine,
        supportingText = supportingText
    )
}

@Composable
fun MyPasswordEditText(
    label: String,
    hint: String = "",
    iconLeading: ImageVector,
    iconLeadingDescription: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit = {},
    hasError: Boolean = false,
    supportingText :  @Composable (() -> Unit)? = null,
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var showPassword by remember { mutableStateOf(value = false) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (8.dp), end = (8.dp)),
        value = text,
        label = { Text(label) },
        placeholder = { Text(hint) },
        onValueChange = { newText ->
            text = newText
            onValueChange(newText.text)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = {
            Icon(
                imageVector = iconLeading,
                contentDescription = iconLeadingDescription
            )
        },
        trailingIcon = {
            if (showPassword) {
                IconButton(onClick = { showPassword = false }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.visibility_off),
                        contentDescription = "hide_password"
                    )
                }
            } else {
                IconButton(onClick = { showPassword = true }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.visibility),
                        contentDescription = "show_password"
                    )
                }
            }
        },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        singleLine = true,
        isError = hasError,
        supportingText = supportingText
    )
}


@Composable
fun MyVerticalSpacer(height: Int) {
    Spacer(
        modifier = Modifier
            .height(height.dp)
    )
}

@Composable
fun MyHorizontalSpacer(width: Int) {
    Spacer(
        modifier = Modifier
            .width(width.dp)
    )
}

@Composable
fun MyCardInventory(
    modifier : Modifier = Modifier,
    item: String,
    number: Int,
    //modifier: Modifier
) {
    /*MyCard{
        Text(
            text = item,
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth()
               // .align(Alignment.CenterHorizontally),
            ,
            textAlign = TextAlign.Center
        )

        Text(
            text = number.toString(),
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center

        )
    }*/

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier,
            //.width((LocalConfiguration.current.screenWidthDp / 4).dp),
        colors = CardDefaults.cardColors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)/*CardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            disabledContentColor = MaterialTheme.colorScheme.primaryContainer
                        )*/
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = item,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            MyVerticalSpacer(height = 5)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = number.toString(),
            textAlign = TextAlign.Center,
                )

        }
    }
}

@Composable
fun MyCard(
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit
){
    Card(
        modifier = modifier
            .animateContentSize()
            //.padding(8.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(10.dp),
            )
            .width(
                (LocalConfiguration.current.screenWidthDp / 4).dp
            ),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContentColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(20),

    ) {
        content()
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyPreview() {

}