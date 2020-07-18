package com.ionwayinc.project.notify.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.ionwayinc.project.notify.R
import kotlinx.android.synthetic.main.activity_admin_login.*
import kotlinx.android.synthetic.main.activity_main.*

class AdminLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        //code for login part start
        adminLoginButton.setOnClickListener {
            var username = usernameInputText.text.toString().trim()
            var password = passwordInputText.text.toString().trim()

            if ( !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) ) {
                loginUser(username, password)
            } else {
                Toast.makeText(this, "One or multiple fields are empty", Toast.LENGTH_LONG).show()
            }
        }
        //code for login part over
    }

    private fun loginUser(username: String, password: String) {
        if (username == "admin" && password == "admin123") {
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_LONG).show()

            var adminDashboardIntent = Intent(this, AdminAnnouncements::class.java)
            startActivity(adminDashboardIntent)
            finish()
        } else {
            Toast.makeText(this, "Login Failed!", Toast.LENGTH_LONG).show()
        }
    }

}
