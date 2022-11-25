package com.example.mymovie.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mymovie.R
import com.example.mymovie.databinding.FragmentProfileBinding
import com.example.mymovie.datastore.DataStoreManager
import com.example.mymovie.model.LoginViewModel
import com.example.mymovie.model.ViewModelUser
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val REQUEST_CODE_PERMISSION = 100
    private lateinit var pref: DataStoreManager
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModelLoginPref: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChoose.setOnClickListener {
            checkingPermissions()
        }

        binding.buttonLogout.setOnClickListener {
            alertDialog()
        }

        binding.buttonUpdate.setOnClickListener {
            updateUser()
            this.findNavController().navigate(R.id.action_profileFragment_to_moviesFragment)
        }
    }


    private fun updateUser() {
        var username = binding.etUsername.text.toString()
        var password = binding.etPassword.text.toString()
        var address = binding.etAddress.text.toString()
        var age = binding.etAge.text.toString()
        val viewModel = ViewModelProvider(this).get(ViewModelUser::class.java)
        viewModelLoginPref.getUser().observe(this.requireActivity(), {
            viewModel.updateApiUser(it.id, it.name, username, password, age, address)
        })
        viewModel.updateLiveDataUser().observe(this.requireActivity(), {
            if (it != null){
                Toast.makeText(this.requireActivity(), "Update Data Success", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun alertDialog(){
        val builder = android.app.AlertDialog.Builder(requireActivity())

        builder.setTitle("Logout")

        builder.setMessage("Are You Sure ?")

        builder.setNegativeButton("No"){dialogInterface, which ->
            Toast.makeText(requireActivity(),"No", Toast.LENGTH_LONG).show()
        }

        builder.setPositiveButton("Yes"){dialogInterface, which->
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            Toast.makeText(requireActivity(),"Logout", Toast.LENGTH_LONG).show()
        }
        val alertDialog: android.app.AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }


    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleCameraImage(result.data)
            }
        }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            binding.ivImage.setImageURI(result)
        }

    private fun checkingPermissions() {
        if (isGranted(
                this.requireActivity(),
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,
            )
        ) {
            chooseImageDialog()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this.requireActivity())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", this.requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun chooseImageDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this.requireActivity())
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }

    private fun openGallery() {
        this.requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(cameraIntent)
    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        binding.ivImage.setImageBitmap(bitmap)

    }

}