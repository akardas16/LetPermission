package com.akardas.letscheckpermission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class LetsCheckPermission(private val activity: AppCompatActivity) {


    fun Map<String,Status>.allGranted():Boolean{
        return this.values.all { it == Status.GRANTED_ALREADY }
    }

    fun Map<String,Status>.allDenied():Boolean{
        return this.values.all { it != Status.GRANTED_ALREADY }
    }
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun shouldAskPermission(permission: String): Boolean {
        if (shouldAskPermission()) {
            val permissionResult = ActivityCompat.checkSelfPermission(activity, permission)
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    fun of(permission: String, onChangedStatus:(status: Status) -> Unit):ActivityResultLauncher<String> {
        val initial = if (shouldAskPermission(permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                Status.DENIED_WITH_RATIONALE
            } else {
                if (LetPermissionPreferences(activity).isFirstTimeAsking(permission)) {
                    Status.NOT_ASKED
                } else {
                    Status.DENIED_WITH_NEVER_ASK
                }
            }
        } else {
            Status.GRANTED_ALREADY
        }
        onChangedStatus(initial)

        return activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted.not()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    onChangedStatus(Status.DENIED_WITH_RATIONALE)
                    LetPermissionPreferences(activity).firstTimeAsking(permission,false)
                } else {
                    if (LetPermissionPreferences(activity).isFirstTimeAsking(permission)){
                        onChangedStatus(Status.NOT_ASKED)
                    }else{
                        onChangedStatus(Status.DENIED_WITH_NEVER_ASK)
                    }

                }
            } else {
                onChangedStatus(Status.GRANTED_ALREADY)
            }
        }
    }

    fun ofs(permissions:List<String>, onChangedStatus:(statusList:Map<String, Status>) -> Unit):ActivityResultLauncher<Array<String>> {
        val allStatus = mutableMapOf<String, Status>()
        permissions.forEach {
            val status = if (shouldAskPermission(it)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {
                    Status.DENIED_WITH_RATIONALE
                } else {
                    if (LetPermissionPreferences(activity).isFirstTimeAsking(it)) {
                        Status.NOT_ASKED
                    } else {
                        Status.DENIED_WITH_NEVER_ASK
                    }
                }
            } else {
                Status.GRANTED_ALREADY
            }
            allStatus[it] = status
        }
        onChangedStatus(allStatus)

        return activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val permissionsStatus = mutableMapOf<String, Status>()
            result.forEach {
                if (it.value.not()) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it.key)) {
                        permissionsStatus[it.key] = Status.DENIED_WITH_RATIONALE
                        LetPermissionPreferences(activity).firstTimeAsking(it.key,false)
                    } else {
                        if (LetPermissionPreferences(activity).isFirstTimeAsking(it.key)){
                            permissionsStatus[it.key] = Status.NOT_ASKED
                        }else{
                            permissionsStatus[it.key] = Status.DENIED_WITH_NEVER_ASK
                        }

                    }
                } else {
                    permissionsStatus[it.key] = Status.GRANTED_ALREADY
                }
            }
            onChangedStatus(permissionsStatus)
        }
    }


}
fun Context.openAppSystemSettings() = startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null)))

enum class Status{
    INITIAL,GRANTED_ALREADY,DENIED_WITH_RATIONALE,NOT_ASKED,DENIED_WITH_NEVER_ASK
}