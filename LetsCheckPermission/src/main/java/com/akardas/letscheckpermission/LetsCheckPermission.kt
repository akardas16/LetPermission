package com.akardas.letscheckpermission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.security.Permissions


class LetsCheckPermission(private val context: Context) {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun shouldAskPermission(permission: String): Boolean {
        if (shouldAskPermission()) {
            val permissionResult = ActivityCompat.checkSelfPermission(context, permission)
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    fun of(permission: String, permissionCallBack : (permissionStatus:Status) -> Unit) {
        if (shouldAskPermission(permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((context as AppCompatActivity), permission)) {
                permissionCallBack.invoke(Status.DENIED)
            } else {
                if (LetPermissionPreferences(context).isFirstTimeAsking(permission)) {
                    permissionCallBack.invoke(Status.NOT_ASKED)
                } else {
                    permissionCallBack.invoke(Status.DENIED_WITH_NEVER_ASK)
                }
            }
        } else {
            permissionCallBack.invoke(Status.GRANTED)
        }
    }


}

enum class Status{
    GRANTED,DENIED,NOT_ASKED,DENIED_WITH_NEVER_ASK
}