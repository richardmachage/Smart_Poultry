package com.example.smartpoultry.domain.permissions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smartpoultry.activity.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)

class PermissionService @Inject constructor(
    @ApplicationContext val context: Context
)  {

    //check for permission
    fun checkIfPermissionGranted(permission : String) = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun showRequestPermissionRationale(permission: String, rationale : String, activity : Activity) : Boolean{
        //if permission is not granted and the rtionale needs to be shown return true, otherwise false
        if (!checkIfPermissionGranted(permission) && ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)) {
            AlertDialog.Builder(activity)
                .setTitle("Permission Required")
                .setMessage(rationale)
                .setPositiveButton("OK"){_,_->
                    //requesting for permission
                }
                .setNegativeButton("Cancel"){dialog, _->
                    //handle other cases on dismiss
                    dialog.dismiss()
                }
                .show()
            return true
        }
        else{ return false}
    }
}