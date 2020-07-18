package com.ionwayinc.project.notify.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ionwayinc.project.notify.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class Registration : AppCompatActivity() {

    //
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null
    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mAuth = FirebaseAuth.getInstance()

        //onclicklistener starts
        signUpButton.setOnClickListener {

            userProfileLayout3.animate().alpha(0.25f)

            var email = emailInputText.text.toString().trim()
            var password = passwordInputText.text.toString().trim()
            var displayName = displayNameInputText.text.toString().trim()
            var registrationId = registrationIdInputText.text.toString().trim()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(displayName) && !TextUtils.isEmpty(registrationId)) {
                createAccount(email, password, displayName, registrationId)
            } else {
                userProfileLayout3.animate().alpha(1f)
                Toast.makeText(this, "One or multiple fields are empty", Toast.LENGTH_LONG).show()
            }
        }
        //onclicklistener ends

    } //on create ending bracket

    //create account function starts
    fun createAccount(email: String, password: String, displayName: String, registrationId: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task: Task<AuthResult> ->

                    if (task.isSuccessful) {
                        var currentUser = mAuth!!.currentUser
                        var userId = currentUser!!.uid

                        mDatabase = FirebaseDatabase.getInstance().reference
                                .child("Users")
                                .child(userId)

                        /*
                            Users
                                - userId
                                    display_name
                                    registration_id
                                    email_address
                                    image
                                    thumb_image
                         */

                        var userObject = HashMap<String, String>()
                        userObject.put("display_name", displayName)
                        userObject.put("registration_id", registrationId)
                        userObject.put("email_address", email)
                        userObject.put("image", "default")
                        userObject.put("thumb_image", "default")

                        mDatabase!!.setValue(userObject).addOnCompleteListener {
                            task: Task<Void> ->
                            if (task.isSuccessful) {
                                userProfileLayout3.animate().alpha(1f)
                                Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_LONG).show()

                                FirebaseAuth.getInstance().signOut()

                                var mainIntent = Intent(this, MainActivity::class.java)
//                                mainIntent.putExtra("name", displayName)
                                startActivity(mainIntent)
                                finish()
                            } else{
                                userProfileLayout3.animate().alpha(1f)
                                Toast.makeText(this, "Sign Up Failed!", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else{
                        userProfileLayout3.animate().alpha(1f)
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                    }
                }
    }
    //create account function ends
}
