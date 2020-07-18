package com.ionwayinc.project.notify.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ionwayinc.project.notify.R
import kotlinx.android.synthetic.main.activity_learn_more.*

class LearnMore : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_more)

        learnMoreWebView.loadUrl("http://ggmsciencecollege.in/collegeprofile.html")
    }
}
