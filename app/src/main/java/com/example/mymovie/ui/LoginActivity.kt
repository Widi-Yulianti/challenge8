package com.example.mymovie.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.mymovie.MainActivity
import com.example.mymovie.datastore.DataStoreManager
import com.example.mymovie.model.LoginViewModel
import com.example.mymovie.model.ViewModelFactory
import com.example.mymovie.model.ViewModelUser
import com.example.mymovie.databinding.ActivityLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: DataStoreManager
    private lateinit var viewModelLoginPref: LoginViewModel
    private lateinit var analytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        analytics = Firebase.analytics

        binding.btnCrash.setOnClickListener {
            throw RuntimeException("Test Crash")
        }

        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            pref = DataStoreManager(this)
            viewModelLoginPref = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val viewModel = ViewModelProvider(this).get(ViewModelUser::class.java)
            viewModel.callGetUser()
            viewModel.getLiveDataUser().observe(this, {
                if (it != null) {
                    for (i in it) {
                        if (i.username == username && i.password == password) {
                            viewModelLoginPref.setUserLogin(true)
                            viewModelLoginPref.saveUser(i.id,i.name,i.username,i.password,i.age,i.address)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "whopsy theres somethings wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }

        val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { res ->
            this.onSignInResult(res)
        }

        binding.apply {
            btnAuth.setOnClickListener {
                // Choose authentication providers
                val providers = arrayListOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.PhoneBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build())

                // Create and launch sign-in intent
                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build()
                signInLauncher.launch(signInIntent)
            }
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Toast.makeText(this,"Login : ${user?.email}", Toast.LENGTH_LONG).show()

            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }



}