package com.ionwayinc.project.notify.activities

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.ionwayinc.project.notify.R
import kotlinx.android.synthetic.main.activity_dashboard.*

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        AnnouncementsButtonId.setOnClickListener {
            startActivity(Intent(this, Announcements::class.java))
            finish()
        }

        InteractionButtonId.setOnClickListener {
            startActivity(Intent(this, Interaction::class.java))
            finish()
        }

        UserProfileButtonId.setOnClickListener {
            startActivity(Intent(this, UserProfile::class.java))
            finish()
        }

        meetTheDevelopersId.setOnClickListener {
            startActivity(Intent(this, Developers::class.java))
            finish()
        }


    }

    //code for exit dialog box on pressing back key
    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setIcon(R.drawable.alert)
                .setTitle("Exit Application?")
                //.setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    //super.onBackPressed();
                    //Or used finish();
                    finish()
                })
                .setNegativeButton("No", null)
                .show()
    }
    //code for exit dialog box on pressing back key over

}
