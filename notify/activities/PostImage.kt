package com.ionwayinc.project.notify.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import com.ionwayinc.project.notify.R
import com.squareup.picasso.Picasso

class PostImage : AppCompatActivity() {
    var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_image)

        imageUrl = intent.extras.get("image").toString()
//
//        var image: ImageView = findViewById(R.id.fullSizeImage)
//        Picasso.get()
//                .load(imageUrl)
//                .into(image)

        var image: WebView = findViewById(R.id.fullSizeImageUrl)

        image.loadUrl(imageUrl)

    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this, Announcements::class.java))

        var id = android.os.Process.myPid()
        android.os.Process.killProcess(id)
    }
}
