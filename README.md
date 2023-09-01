# LetPermission

## Install

* Jitpack

```Kotlin
 allprojects {
   repositories {
        maven { url 'https://jitpack.io' }
    }
 }
```

```Kotlin
implementation 'com.github.akardas16:LetPermission:1.0.3'
```

## Usage

* Make sure you added related permission to Manifest file!

```Kotlin
binding.button.setOnClickListener {

            LetsCheckPermission(this).of(Manifest.permission.READ_CONTACTS){ permissionStatus ->
                when(permissionStatus){
                    Status.GRANTED -> {
                        //Permission has already granted!
                        Toast.makeText(this,"Permission has already granted!",Toast.LENGTH_LONG).show()
                    }
                    Status.DENIED -> {//Showing system permission Pop Up is possible
                        //Permission denied but you still have chance to show permission pop up again
                        Toast.makeText(this,"Permission denied!",Toast.LENGTH_LONG).show()
                        LetPermissionPreferences(this).firstTimeAsking(Manifest.permission.READ_CONTACTS,false)

                        //Request READ_CONTACTS Permission
                        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                    Status.NOT_ASKED -> {//Showing system permission Pop Up is possible
                        //you have not yet requested permission
                        Toast.makeText(this,"no Permission!",Toast.LENGTH_LONG).show()
                        if (LetPermissionPreferences(this).isFirstTimeAsking(Manifest.permission.READ_CONTACTS)){
                            LetPermissionPreferences(this).firstTimeAsking(Manifest.permission.READ_CONTACTS,false)

                            //Request READ_CONTACTS Permission
                            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                        }
                    }
                    Status.DENIED_WITH_NEVER_ASK -> {
                        //You can't open system permission Pop Up! Navigate user to settings to give permission manually
                        Toast.makeText(this,"permission Denied WithNeverAskAgain!",Toast.LENGTH_LONG).show()

                        openAppSystemSettings()
                    }
                }

            }
        }
```

* Navigate user to settings page

```Kotlin
    //If you can't show system permission Pop Up to user, At least you can navigate user to settings page by calling openAppSystemSettings function
    private fun Context.openAppSystemSettings() = startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)))
```

* Request your permission
```Kotlin
    requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
```

* You can listen the result from here when user granted o denied permission
```Kotlin
   private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        //You can listen the result from here when user granted o denied permission
        Toast.makeText(this,"is granted: $isGranted",Toast.LENGTH_LONG).show()
    }
```
