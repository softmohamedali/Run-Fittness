package com.example.runfittnes.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.runfittnes.R
import com.example.runfittnes.databinding.FragmentRunBinding
import com.example.runfittnes.databinding.FragmentSettingBinding
import com.example.runfittnes.utils.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding?=null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSettingBinding.inflate(layoutInflater)
        getInfoFromSHP()
        binding.btnApplychangSettingfrag.setOnClickListener {
            if (applyChangesToShearedPre())
            {
                Snackbar.make(binding.root,"applay sucessed",Snackbar.LENGTH_SHORT)
                    .setAction("OK",{})
                    .show()
            }
            else{
                Snackbar.make(binding.root,"Please enter all fields",Snackbar.LENGTH_SHORT)
                    .setAction("OK",{})
                    .show()
            }
        }
        return binding.root
    }

    private fun getInfoFromSHP()
    {
        val name=sharedPreferences.getString(Constants.KEY_NAME_SHP,"")
        val wight=sharedPreferences.getFloat(Constants.KEY_WIGHT_SHP,75f)

        binding.etNameSettingfrag.setText(name)
        binding.etWightSettingfrag.setText(wight.toString())
    }

    private fun applyChangesToShearedPre():Boolean
    {
        val name=binding.etNameSettingfrag.text.toString()
        val wight=binding.etWightSettingfrag.text.toString()
        if (name.isEmpty()||wight.isEmpty())
        {
            return false
        }

        sharedPreferences.edit()
            .putString(Constants.KEY_NAME_SHP,name)
            .putFloat(Constants.KEY_WIGHT_SHP,wight.toFloat())
            .apply()

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}