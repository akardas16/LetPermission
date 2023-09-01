package com.akardas.letpermission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.akardas.letpermission.databinding.ActivityMainBinding
import com.akardas.letscheckpermission.LetPermissionPreferences
import com.akardas.letscheckpermission.LetsCheckPermission
import com.akardas.letscheckpermission.Status


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

         val cameraLauncher = LetsCheckPermission(this)
           .of(Manifest.permission.READ_CONTACTS){status ->
               binding.permissionStatus.text = status.name
               when(status){
                   Status.GRANTED_ALREADY -> {
                       //Permission has already granted!
                       Toast.makeText(this,"Permission has already granted!",Toast.LENGTH_LONG).show()
                   }
                   Status.DENIED_WITH_RATIONALE -> {//Showing system permission Pop Up is possible
                       //Permission denied but you still have chance to show permission pop up again
                       Toast.makeText(this,"Permission denied!",Toast.LENGTH_LONG).show()

                       //Request READ_CONTACTS Permission
                   }
                   Status.NOT_ASKED -> {//Showing system permission Pop Up is possible
                       //you have not yet requested permission
                       Toast.makeText(this,"not asked!",Toast.LENGTH_LONG).show()

                   }
                   Status.DENIED_WITH_NEVER_ASK -> {
                       //You can't open system permission Pop Up! Navigate user to settings to give permission manually
                       Toast.makeText(this,"permission Denied WithNeverAskAgain!",Toast.LENGTH_LONG).show()

                       //openAppSystemSettings()
                   }
               }
        }



        binding.button.setOnClickListener {
            cameraLauncher.launch(Manifest.permission.READ_CONTACTS)
        }


    }



}