package com.example.freemates_android

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.freemates_android.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initNavigation()

        printKeyHash(this)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.placeInfoFragment -> { // newFragment에서 툴바 숨김
                    binding.tbContainerMain.visibility = View.GONE
                }
                R.id.searchFragment -> {
                    binding.tbContainerMain.visibility = View.GONE
                }
                else -> {
                    binding.tbContainerMain.visibility = View.VISIBLE
                }
            }
        }

        binding.ibtnSearchButtonMain.setOnClickListener {
            navController.navigate(R.id.searchFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.flContainer_Main) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    fun printKeyHash(context: Context) {
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = info.signingInfo?.apkContentsSigners

            if (signatures != null) {
                for (signature in signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                    Log.d("KeyHash", "keyHash: $keyHash")
                }
            }
        } catch (e: Exception) {
            Log.e("KeyHash", "Error getting KeyHash", e)
        }
    }

}