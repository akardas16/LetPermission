package com.akardas.letpermission

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akardas.letpermission.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {

            LetPermission(this).checkingStatusOf(Manifest.permission.READ_CONTACTS){ permissionStatus ->
                when(permissionStatus){
                    Status.GRANTED -> {
                        //Permission successfully granted!
                        Toast.makeText(this,"Permission granted!",Toast.LENGTH_LONG).show()
                    }
                    Status.DENIED -> {//Showing Pop up is possible
                        //Permission denied but you still have chance to show permission pop up again
                        Toast.makeText(this,"Permission denied!",Toast.LENGTH_LONG).show()
                        LetPermissionPreferences(this).firstTimeAsking(Manifest.permission.READ_CONTACTS,false)
                        RequestPermission.withResult(this,Manifest.permission.READ_CONTACTS){ isGranted ->
                            //You can listen the result from here when user granted o denied permission
                            Toast.makeText(this,"is granted $isGranted",Toast.LENGTH_LONG).show()
                        }
                    }
                    Status.NOT_ASKED -> {//Showing Pop up is possible
                        //you have not yet requested permission
                        Toast.makeText(this,"no Permission!",Toast.LENGTH_LONG).show()
                        if (LetPermissionPreferences(this).isFirstTimeAsking(Manifest.permission.READ_CONTACTS)){
                            LetPermissionPreferences(this).firstTimeAsking(Manifest.permission.READ_CONTACTS,false)
                            RequestPermission.withResult(this,Manifest.permission.READ_CONTACTS){ isGranted ->
                                //You can listen the result from here when user granted o denied permission
                                Toast.makeText(this,"is granted $isGranted",Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    Status.DENIED_WITH_NEVER_ASK -> {
                        //You can't open permission pop up from android system! Navigate user to settings to give permission manually
                        Toast.makeText(this,"permission Denied WithNeverAskAgain!",Toast.LENGTH_LONG).show()
                    }
                }

            }
        }


    }


}