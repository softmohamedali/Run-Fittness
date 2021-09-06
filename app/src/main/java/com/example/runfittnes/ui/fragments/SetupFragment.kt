package com.example.runfittnes.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.room.Index
import com.example.runfittnes.R
import com.example.runfittnes.databinding.FragmentSetupBinding
import com.example.runfittnes.utils.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment(){

    private var _binding: FragmentSetupBinding?=null
    private val binding get() = _binding!!

    @Inject
    lateinit var shp:SharedPreferences

    @set:Inject
    var misfirst:Boolean=true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSetupBinding.inflate(layoutInflater)

        if (!misfirst)
        {
            val navOption=NavOptions.Builder()
                    .setPopUpTo(R.id.setupFragment,true)
                    .build()
            findNavController().navigate(R.id.action_setupFragment_to_runFragment,savedInstanceState,navOption)

        }

        binding.button.setOnClickListener {
            if (saveInfoInShearedPrefrences())
            {
                val navOption=NavOptions.Builder()
                        .setPopUpTo(R.id.setupFragment,true)
                        .build()
                findNavController().navigate(R.id.action_setupFragment_to_runFragment,savedInstanceState,navOption)
            }else{
                ////////////////////////////////////////////
                Snackbar.make(binding.root,"you Should Enter your info !!",Snackbar.LENGTH_SHORT)
                        .setAction("OK",{}).show()
            }
        }
        return binding.root
    }

    private fun saveInfoInShearedPrefrences():Boolean{
        val name=binding.etNameSetupfrag.text.toString()
        val wight=binding.etWightSetupfrag.text.toString()
        if (name.isEmpty()&&wight.isEmpty())
        {
            return false
        }
        shp.edit()
                .putBoolean(Constants.KEY_ISFIRSTTIME_SHP,false)
                .putFloat(Constants.KEY_WIGHT_SHP,wight.toFloat())
                .putString(Constants.KEY_NAME_SHP,name)
                .apply()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}