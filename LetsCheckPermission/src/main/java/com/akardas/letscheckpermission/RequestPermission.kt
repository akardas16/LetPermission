package com.akardas.letscheckpermission

import android.app.Activity
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

object RequestPermission {

    fun withResult(activity: Activity, permission:String, result:(isGranted:Boolean) -> Unit){
        activity as AppCompatActivity
         val requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
             result.invoke(isGranted)
        }
        requestPermissionLauncher.launch(permission)
    }

}