# LetPermission

* All situations handled

```Kotlin
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
                        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                    Status.NOT_ASKED -> {//Showing Pop up is possible
                        //you have not yet requested permission
                        Toast.makeText(this,"no Permission!",Toast.LENGTH_LONG).show()
                        if (LetPermissionPreferences(this).isFirstTimeAsking(Manifest.permission.READ_CONTACTS)){
                            LetPermissionPreferences(this).firstTimeAsking(Manifest.permission.READ_CONTACTS,false)
                            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        }
                    }
                    Status.DENIED_WITH_NEVER_ASK -> {
                        //You can't open permission pop up from android system! Navigate user to settings to give permission manually
                        Toast.makeText(this,"permission Denied WithNeverAskAgain!",Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
```

* You can listen the result from here when user granted o denied permission

```Kotlin
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
```
