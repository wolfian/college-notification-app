package com.ionwayinc.project.notify.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.ionwayinc.project.notify.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class MainActivity : AppCompatActivity() {

    //design initialisation start
    var usernameWrapper: TextInputLayout? = null
    var passwordWrapper: TextInputLayout? = null
    //design initialisation over

    //login initialisation start
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null

    var user: FirebaseUser? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null
    //login initialisation over


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //design code start
        this.usernameWrapper = (TextInputLayout(this)).findViewById(R.id.usernameWrapper)
        this.passwordWrapper = (TextInputLayout(this)).findViewById(R.id.passwordWrapper)
        usernameWrapper?.setHint("Username")
        passwordWrapper?.setHint("Password")
        //design code over


        //code for passing intent to create account activity starts
        createAccountButton.setOnClickListener {
            startActivity(Intent(this, Registration::class.java))
        }
        //code for passing intent to create account activity starts


        mAuth = FirebaseAuth.getInstance()


        //code to check whether user is logged in or not start
        mAuthListener = FirebaseAuth.AuthStateListener {
            firebaseAuth: FirebaseAuth ->
            user = firebaseAuth.currentUser

            if (user!=null) {
                startActivity(Intent(this, Dashboard::class.java))
                finish()
            }

        }
        //code to check whether user is logged in or not over


        //
        learnMoreButton.setOnClickListener {
            startActivity(Intent(this, LearnMore::class.java))
        }
        //


        //code for login part start
        loginButton.setOnClickListener {
            var email = emailLoginInput.text.toString().trim()
            var password = passwordLoginInput.text.toString().trim()

            if ( !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) ) {
                userProfileLayout2.animate().alpha(0.25f)
                loginUser(email, password)
            } else {
                userProfileLayout2.animate().alpha(1f)
                Toast.makeText(this, "One or multiple fields are empty", Toast.LENGTH_LONG).show()
            }
        }
        //code for login part over

        //opening admin login
        loginAsAdminId.setOnClickListener {
            startActivity(Intent(this, AdminLogin::class.java))
            finish()
        }

    } //on create ending bracket



    //loginUser function start
    private fun loginUser(email: String, password: String) {
        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        userProfileLayout2.animate().alpha(1f)
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_LONG).show()

//                        var username = email.split("@")[0]

                        var dashboardIntent = Intent(this, Dashboard::class.java)
                        startActivity(dashboardIntent)
                        finish()
                    } else {
                        userProfileLayout2.animate().alpha(1f)
                        Toast.makeText(this, "Login Failed!", Toast.LENGTH_LONG).show()
                    }
                }
    }
    //loginUser function over


    //adding and removing AuthDtateListener start
    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        mAuth!!.removeAuthStateListener(mAuthListener!!)
    }
    //adding and removing AuthDtateListener over


    //code for exit dialog box on pressing back key
    override fun onBackPressed() {
//        var id = android.os.Process.myPid()
//        android.os.Process.killProcess(id)
        System.exit(0)
    }
    //code for exit dialog box on pressing back key over

}