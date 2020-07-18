package com.ionwayinc.project.notify.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.ionwayinc.project.notify.R
import kotlinx.android.synthetic.main.activity_add_post.*
import kotlinx.android.synthetic.main.post_row.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class AddPost : AppCompatActivity() {
    var GALLERY_ID: Int = 1
    var uri: Uri? = null
    var mStorageReference: StorageReference? = null
    var mDatabaseRef: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)


        mStorageReference = FirebaseStorage.getInstance().reference
        mDatabaseRef = FirebaseDatabase.getInstance().reference.child("PostDescription")


        addimageid.setOnClickListener {

            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)

        }


        sendbuttonid.setOnClickListener {

            userProfileLayout4.animate().alpha(0.25f)

            //checking time and date
            var currentTime = LocalDateTime.now()
            var currentDate = LocalDate.now()
            var timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
            var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            var formattedTime = currentTime.format(timeFormatter)
            var formattedDate = currentTime.format(dateFormatter)

            var dateTimeValue = "$formattedTime; $formattedDate"
            //

            var descriptionText = descriptionid.text.toString().trim()

            try {

                if (!TextUtils.isEmpty(descriptionText)) {
                    var filePath = mStorageReference!!.child("posts").child(uri!!.lastPathSegment)
                    filePath.putFile(uri!!).addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
                        if (task.isSuccessful) {
                            var downloadUrl = task.result.downloadUrl.toString()

                            userProfileLayout4.animate().alpha(1f)
                            Toast.makeText(this, "Upload Complete", Toast.LENGTH_LONG).show()

                            var newPost: DatabaseReference = mDatabaseRef!!.push()
                            newPost.child("desc").setValue(descriptionText)
                            newPost.child("image").setValue(downloadUrl)
                            newPost.child("time").setValue(dateTimeValue)

                            startActivity(Intent(this, AdminAnnouncements::class.java))
                            finish()
                        } else {
                            userProfileLayout4.animate().alpha(1f)
                            Toast.makeText(this, "Upload Failed", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Empty Description", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                //Toast.makeText(this, "Image not selected", Toast.LENGTH_LONG).show()
                if (!TextUtils.isEmpty(descriptionText)) {
                    Toast.makeText(this, "Upload Complete", Toast.LENGTH_LONG).show()

                    var newPost: DatabaseReference = mDatabaseRef!!.push()
                    newPost.child("desc").setValue(descriptionText)
                    newPost.child("image").setValue("https://vignette.wikia.nocookie.net/plantsvszombies/images/2/24/NoImageAvailable.jpg/revision/latest?cb=20151031232740")
                    newPost.child("time").setValue(dateTimeValue)

//                    post_image_id.setBackgroundResource(R.drawable.noimageavailable)

//                    startActivity(Intent(this, AdminAnnouncements::class.java))
                    finish()
                }
                else {
                    userProfileLayout4.animate().alpha(1f)
                    Toast.makeText(this, "Upload Failed", Toast.LENGTH_LONG).show()
                }
            }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK){
            uri = data!!.data
            addimageid.setImageURI(uri)
        }

    }


}
