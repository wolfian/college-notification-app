package com.ionwayinc.project.notify.activities

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ionwayinc.project.notify.R
import android.content.Intent
import android.content.ActivityNotFoundException
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_developers.*


class Developers : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_developers)

        instaLink1.setOnClickListener {
            val uri = Uri.parse("http://instagram.com/_u/abhi_salotra")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)

            likeIng.`package` = "com.instagram.android"

            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/abhi_salotra")))
            }
        }

        instaLink2.setOnClickListener {
            val uri = Uri.parse("http://instagram.com/_u/paarthmanhastr")
            val likeIng = Intent(Intent.ACTION_VIEW, uri)

            likeIng.`package` = "com.instagram.android"

            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/paarthmanhastr")))
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.return_button, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        startActivity(Intent(this, Dashboard::class.java))
        finish()

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this, Dashboard::class.java))
        finish()
    }

}
