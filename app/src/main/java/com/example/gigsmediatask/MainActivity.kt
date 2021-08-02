package com.example.gigsmediatask

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.gigsmediatask.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration

    val PERMISSION_REQUEST_CODE: Int = 1001
    private var permissonDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navigationView, navController)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data =
                    Uri.parse(String.format("package:%s", applicationContext.packageName))
                startActivityForResult(intent, 2296)

            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivityForResult(intent, 2296)
            }
        } else {
            if (!permissionsGranted()) {
                makeRequest()
                return
            }
        }
    }

    protected fun makeRequest() {
        if (permissonDialog == null) {
            runOnUiThread {
                permissonDialog = AlertDialog.Builder(this).setTitle("Permissions")
                    .setMessage("This application requires permissions to be granted in order to continue")
                    .setPositiveButton(
                        android.R.string.ok
                    ) { dialog, which ->
                        permissonDialog!!.dismiss()
                        permissonDialog = null
                        val permissions = arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            permissions,
                            PERMISSION_REQUEST_CODE
                        )
                    }.setNegativeButton(
                        android.R.string.cancel
                    ) { dialog: DialogInterface?, which: Int ->
                        permissonDialog!!.dismiss()
                        permissonDialog = null
                        finish()
                        System.exit(0)
                    }.setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false).show()
            }
        }
    }

    fun permissionsGranted(): Boolean {
        var allPermissionsGranted = true
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            val hasWriteExternalPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            val hasReadExternalPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasWriteExternalPermission || !hasReadExternalPermission ) {
                allPermissionsGranted = false
            }
        }
        return allPermissionsGranted
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            2296 -> if (resultCode == 0) {
                if (!permissionsGranted()) {
                    makeRequest()
                    return
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                var i = 0
                while (i < grantResults.size) {
                    if (grantResults.size > 0 && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        val permission = permissions[i]
                        val showRationale = shouldShowRequestPermissionRationale(permission!!)
                        if (!showRationale) {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                packageName, null
                            )
                            intent.data = uri
                            startActivity(intent)
                            Toast.makeText(
                                this,
                                "Please consider granting all required permissions",
                                Toast.LENGTH_LONG
                            ).show()
                            break
                        } else {
                            Toast.makeText(
                                this,
                                "Please consider granting all required permissions",
                                Toast.LENGTH_LONG
                            ).show()
                            break
                        }
                    }
                    i++
                }
            }
        }
    }

    private fun init() {
        drawerLayout = dataBinding.drawer
        navigationView = dataBinding.navigation

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_task1,
            R.id.navigation_task2,
            R.id.navigation_task3,
            R.id.navigation_task4,
            R.id.navigation_update
        )
            .setDrawerLayout(drawerLayout)
            .build()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    /*fun download(view: View) {
        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<DownloadPdfWork>()
                .build()

        WorkManager
            .getInstance(this)
            .enqueue(uploadWorkRequest)
    }*/


}