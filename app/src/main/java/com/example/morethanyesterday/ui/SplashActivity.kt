package com.example.morethanyesterday.ui

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setPermissions()
    }

    private fun setPermissions() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val backgroundExecutable: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
                val mainExecutor = ContextCompat.getMainExecutor(this@SplashActivity)

                backgroundExecutable.schedule({
                    mainExecutor.execute {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }, 1, TimeUnit.SECONDS)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                AlertDialog.Builder(this@SplashActivity)
                    .setMessage("권한 거절로 인해 기능이 제한됩니다.")
                    .setPositiveButton("권한 설정하러 가기") { dialog, which ->
                        try {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                    .setNegativeButton("종료") { dialog, which ->
                        finish()
                    }
                    .show()

                Toast.makeText(this@SplashActivity, "권한 거부됨", Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.with(this@SplashActivity)
            .setPermissionListener(permissionListener)
            .setRationaleMessage("정확한 날씨 정보를 위해 권한을 허용해주세요.")
            .setDeniedMessage("권한을 거부하셨습니다. [앱 설정]->[권한] 항목에서 권한을 허용해주세요.")
            .setPermissions(*PERMISSIONS)
            .check()
    }

    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}