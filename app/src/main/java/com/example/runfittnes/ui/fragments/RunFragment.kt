package com.example.runfittnes.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.runfittnes.R
import com.example.runfittnes.databinding.FragmentRunBinding
import com.example.runfittnes.utils.Constants
import com.example.runfittnes.utils.MyUtility
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class RunFragment : Fragment() ,EasyPermissions.PermissionCallbacks {

    private var _binding:FragmentRunBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding= FragmentRunBinding.inflate(layoutInflater)
        requstREquiredPermission()
        binding.fabRunfragAddtrack.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
    fun requstREquiredPermission(){
        if (MyUtility.hasRequirePermission(requireContext())){
            return
        }else{
            if (Build.VERSION.SDK_INT< Build.VERSION_CODES.Q)
            {
                EasyPermissions.requestPermissions(
                        this,
                        "you need this permission to work tjis app",
                        Constants.REQUSTCODE_PERMISSION_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                )
            }
            else{
                EasyPermissions.requestPermissions(
                        this,
                        "you need this permission to work tjis app",
                        Constants.REQUSTCODE_PERMISSION_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms))
        {
            AppSettingsDialog.Builder(this).build().show()
        }
        else{
            requstREquiredPermission()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

}