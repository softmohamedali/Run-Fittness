package com.example.runfittnes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.runfittnes.R
import com.example.runfittnes.databinding.ActivityMainBinding
import com.example.runfittnes.databinding.FragmentRunBinding
import com.example.runfittnes.ui.fragments.TrackingFragment
import com.example.runfittnes.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding?=null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title=""
        navToTrackIfItentSaidThat(intent)


        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.fragment))
        findNavController(R.id.fragment).addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.setupFragment ->{
                    binding.bottomNavigationView.visibility=View.GONE
                    binding.toolbar.visibility=View.GONE
                }
                R.id.trackingFragment->{
                    binding.bottomNavigationView.visibility=View.GONE
                    binding.toolbar.visibility=View.VISIBLE
                }
                else ->{
                    binding.bottomNavigationView.visibility=View.VISIBLE
                    binding.toolbar.visibility=View.VISIBLE
                }
            }
        }


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navToTrackIfItentSaidThat(intent)
    }

     /**
      *this function wil listen to the action in intent and if we click in notificatio
      *will go to the app and go to track Fragment
     **/
    private fun navToTrackIfItentSaidThat(intent:Intent?){
        if(intent?.action==Constants.ACTION_PENDING_TO_TRCKFRAG){
            findNavController(R.id.fragment).navigate(R.id.action_global_to_TruckFrag)
        }
    }
}