package com.example.smartpoultry.presentation.composables.dropDownMenus

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartpoultry.utils.Countries

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountriesDropDownMenu(
    modifier: Modifier = Modifier,
    onItemClick: (Countries) -> Unit,
){
    val listOfCountries = Countries.entries.toList()
    var selectedItem by remember { mutableStateOf("Select your Country") }
    var expanded by  remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = !expanded}) {
        OutlinedTextField(
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(start = (6.dp), end = (6.dp)),
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            label = { Text(text = "Countries") },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOfCountries.forEach { country ->
                DropdownMenuItem(
                    text = { Text(text = country.countryName) },
                    onClick = {
                        selectedItem = country.countryName
                        onItemClick(country)
                        expanded = false
                    }
                )
            }
        }
    }

}
