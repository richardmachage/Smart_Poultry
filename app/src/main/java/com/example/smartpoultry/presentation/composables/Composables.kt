package com.example.smartpoultry.presentation.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.R


@Composable
fun NormText(text: String) {
    Text(
        modifier = Modifier.padding(4.dp),
        text = text,
        maxLines = 1,
    )
}

@Composable
fun NormButton(
    onButtonClick: () -> Unit,
    btnName: String,
    modifier: Modifier = Modifier,
    enabled : Boolean = true
) {
    Button(
        onClick = onButtonClick,
        modifier = modifier
            //.fillMaxWidth()
            .padding(8.dp),
        enabled = enabled
    ) {
        NormText(text = btnName)
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
    label: String = "",
    hint: String = "",
    iconLeading: ImageVector,
    iconLeadingDescription: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
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
            Icon(
                imageVector = iconLeading,
                contentDescription = iconLeadingDescription
            )
        },
        trailingIcon = {
            IconButton(onClick = { text = TextFieldValue("") }) {
                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear_input")
            }
        },
        singleLine = true
    )
}

@Composable
fun MyPasswordEditText(
    label: String,
    hint: String = "",
    iconLeading: ImageVector,
    iconLeadingDescription: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit = {}
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
        singleLine = true
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
    item: String,
    number: Int,
    //modifier: Modifier
) {
    MyCard{
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
    }
}

@Composable
fun MyCard(
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit
){
    Card(
        modifier = modifier
            .padding(8.dp)
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