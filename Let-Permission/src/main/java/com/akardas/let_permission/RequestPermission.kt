package com.akardas.let_permission

import android.app.Activity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

object RequestPermission {

    fun withResult(activity: AppCompatActivity, permission:String, result:(isGranted:Boolean) -> Unit){
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