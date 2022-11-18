package com.akardas.letpermission

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

object RequestPermission {

    fun withResult(activity: MainActivity, permission:String, result:(isGranted:Boolean) -> Unit){
         val requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
             result.invoke(isGranted)
          /*  if (isGranted) {
                // PERMISSION GRANTED
                Toast.makeText(this,"Result granted!", Toast.LENGTH_LONG).show()
            } else {
                // PERMISSION NOT GRANTED
                Toast.makeText(this, "Result Denied", Toast.LENGTH_SHORT).show()
            }*/
        }
        requestPermissionLauncher.launch(permission)
    }

}