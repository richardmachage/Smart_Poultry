package com.example.smartpoultry.presentation.screens.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.smartpoultry.presentation.uiModels.BottomNavigationItem


@Composable
fun NormText(text: String) {
    Text(
        modifier = Modifier.padding(4.dp),
        text = text,
        maxLines = 1,
    )
}

@Composable
fun NormButton(onButtonClick: () -> Unit, btnName: String, modifier: Modifier) {
    Button(
        onClick = onButtonClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        NormText(text = btnName)
    }
}

@Composable
fun MyTextButton(onButtonClick : () -> Unit, btnText : String, modifier:Modifier){
    TextButton(onClick = onButtonClick) {
        NormText(text = btnText)
    }
}
@Composable
fun MyEditText(
    label: String,
    keyboardType: KeyboardType,
    iconLeading: ImageVector,
    iconLeadingDescription: String,

    ) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
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
        singleLine = true

    )
}

@Composable
fun MyEditTextClear(
    label: String,
    hint: String,
    iconLeading: ImageVector,
    iconLeadingDescription: String,
    keyboardType: KeyboardType
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (8.dp), end = (8.dp)),
        value = text,
        label = { Text(text = label) },
        placeholder = { Text(text = hint) },
        onValueChange = { newText -> text = newText },
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
    hint: String,
    iconLeading: ImageVector,
    iconLeadingDescription: String,
    keyboardType: KeyboardType
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
        onValueChange = { newText -> text = newText },
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
fun MyBottomNavBar() {
    val items = listOf<BottomNavigationItem>(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Analytics",
            selectedIcon = ImageVector.vectorResource(R.drawable.analytics_filled),
            unselectedIcon = ImageVector.vectorResource(R.drawable.analytics_outlined)
        ),
        BottomNavigationItem(
            title = "Alerts",
            selectedIcon = ImageVector.vectorResource(R.drawable.notification_filled),
            unselectedIcon = ImageVector.vectorResource(R.drawable.notification_outline)
        ),
        BottomNavigationItem(
            title = "Eggs",
            selectedIcon = ImageVector.vectorResource(R.drawable.egg_filled),
            unselectedIcon = ImageVector.vectorResource(R.drawable.egg_outline),
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    //add code to handle navigation
                },
                icon = {
                    Icon(
                        imageVector = if (selectedItemIndex == index) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                }
            )

        }
    }

}

@Composable
fun MyVerticalSpacer(height : Int){
    Spacer(modifier = Modifier
        .height(height.dp))
}

@Composable
fun MyHorizontalSpacer(width : Int){
    Spacer(modifier = Modifier
        .width(width.dp))
}

@Composable
fun MyCardInventory(
    item : String,
    number : Int,
    //modifier: Modifier
){
    Card (
        modifier = Modifier
            .padding(8.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(10.dp)

            )
            .width(
                (LocalConfiguration.current.screenWidthDp / 4).dp
            )
            .height(100.dp)

    ){
        Text(
            text = item,
            modifier = Modifier
                .padding(6.dp)
                .align(Alignment.CenterHorizontally)
            )

        Text(
            text = number.toString(),
            modifier = Modifier
                .padding(6.dp)
                .align(Alignment.CenterHorizontally)
           // textAlign = TextAlign.Center

        )
    }
}
@Preview(showBackground = true)
@Composable
fun MyPreview() {
    MyCardInventory(item = "Chicken", number =2000 )//, modifier = Modifier)
}