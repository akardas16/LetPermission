package com.akardas.letpermission

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.akardas.letpermission.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val REQUEST_CAMERA = 5
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

  /*      if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                // Show an expanation to the user
            } else {
                if (SessionManager(this).isFirstTimeAsking(Manifest.permission.READ_CONTACTS)){
                    SessionManager(this).firstTimeAsking(Manifest.permission.READ_CONTACTS,false)
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CAMERA)
                }
            }
        } else {
            //permission granted. do your stuff
        }*/

        binding.permBtn.setOnClickListener {
            LetPermission(this).checkingStatusOf(Manifest.permission.READ_CONTACTS){ permissionStatus ->
                when(permissionStatus){
                    Status.GRANTED -> {
                        //Permission successfully granted!
                        Toast.makeText(this,"Permission granted!",Toast.LENGTH_LONG).show()
                    }
                    Status.DENIED -> {//Showing Pop up possible
                        //Permission denied but you still have chance to show permission pop up
                        Toast.makeText(this,"Permission denied!",Toast.LENGTH_LONG).show()
                        LetPermissionPreferences(this).firstTimeAsking(Manifest.permission.READ_CONTACTS,false)
                        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                    Status.NOT_ASKED -> {//Showing Pop up possible
                        //you don't have permission ask for permission
                        Toast.makeText(this,"no Permission!",Toast.LENGTH_LONG).show()
                        if (LetPermissionPreferences(this).isFirstTimeAsking(Manifest.permission.READ_CONTACTS)){
                            LetPermissionPreferences(this).firstTimeAsking(Manifest.permission.READ_CONTACTS,false)
                            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        }
                    }
                    Status.DENIED_WITH_NEVER_ASK -> {
                        //You cant open permission pop up! navigate user to settings
                        Toast.makeText(this,"permission Denied WithNeverAskAgain!",Toast.LENGTH_LONG).show()
                    }
                }

            }
        }


    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // PERMISSION GRANTED
            Toast.makeText(this,"Result granted!",Toast.LENGTH_LONG).show()
        } else {
            // PERMISSION NOT GRANTED
            Toast.makeText(this, "Result Denied", Toast.LENGTH_SHORT).show()
        }
    }


}