package com.forsythe.smartpoultry.presentation.screens.signUp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import com.forsythe.smartpoultry.R
import com.forsythe.smartpoultry.presentation.composables.dropDownMenus.CountriesDropDownMenu
import com.forsythe.smartpoultry.presentation.composables.spacers.MyVerticalSpacer
import com.forsythe.smartpoultry.presentation.composables.textInputFields.MyEditTextClear
import com.forsythe.smartpoultry.presentation.screens.signUp.models.FarmDetailsResponse

@Composable
fun FarmDetails(
    farmDetailsResponse: FarmDetailsResponse ,//= FarmDetailsResponse("",""),
    onResponse : (FarmDetailsResponse) ->Unit = {}
){
    var farmDetails by remember { mutableStateOf(farmDetailsResponse) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        MyEditTextClear( // Farm Name
            value = farmDetails.farmName ,
            label = "Farm Name",
            hint = "eg. Abuya Poultry Farm",
            iconLeading = ImageVector.vectorResource(id = R.drawable.egg_outline),// painterResource(id = R.drawable.egg_outline),//Image(imageVector =, contentDescription = ),
            iconLeadingDescription = "place",
            keyboardType = KeyboardType.Text,
            onValueChange = {text->
                farmDetails = farmDetails.copy(farmName = text)
                onResponse(farmDetails)
            },
            onClear = {
                farmDetails = farmDetails.copy(farmName = "")
                onResponse(farmDetails)
            }
        )

        MyVerticalSpacer(height = 5)

            CountriesDropDownMenu (
                selectedCountry = farmDetails.country,//if(farmDetails.country == null) "Select your Country" else farmDetails.country!!.countryName,//country.countryName.ifBlank { "Select your Country" },
                onItemClick = {
                    farmDetails = farmDetails.copy(country = it)
                    onResponse(farmDetails)
                }
            )
        }

    }
