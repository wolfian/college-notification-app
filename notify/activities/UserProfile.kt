package com.ionwayinc.project.notify.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.ionwayinc.project.notify.R
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_interaction.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.ByteArrayOutputStream
import java.io.File

class UserProfile : AppCompatActivity() {
    var mDatabase: DatabaseReference? =null
    var mCurrentUser: FirebaseUser? = null
    var mStorageref: StorageReference? = null
    var GALLERY_ID: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        //Logout code start
        profileLogoutButtonId.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        //Logout code over


        //Setting display name and image start
        mCurrentUser = FirebaseAuth.getInstance().currentUser

        var userId = mCurrentUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(userId)

        mDatabase!!.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                var displayName = dataSnapshot!!.child("display_name").value.toString()
                var image = dataSnapshot!!.child("image").value.toString()
                var thumbnail = dataSnapshot!!.child("thumb_image").value.toString()

                profileDisplayNameId.text = displayName

                if (!image!!.equals("default")) {
                    Picasso.get()
                            .load(image)
                            .into(profileImageViewId)
                }
            }

            override fun onCancelled(databaseErrorSnapshot: DatabaseError?) {

            }
        })
        //Setting display name and image over


        //Changing profile picture
        profileChangePictureButtonId.setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {

            var image: Uri = data!!.data
            CropImage.activity(image)
                    .setAspectRatio(1, 1)
                    .start(this)
        }

        if (requestCode === CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)

            if (resultCode === Activity.RESULT_OK) {
                userProfileLayout1.animate().alpha(0.25f)

                val resultUri = result.uri

                val userId = mCurrentUser!!.uid
                var thumbFile = File(resultUri.path)

                var thumbBitmap = Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(65)
                        .compressToBitmap(thumbFile)

                //uploading images to firebase
                var byteArray = ByteArrayOutputStream()
                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
                var thumbByteArray: ByteArray
                thumbByteArray = byteArray.toByteArray()

                mStorageref = FirebaseStorage.getInstance().reference

                var filePath = mStorageref!!.child("profile_images")
                        .child("$userId.jpg")

                //creating another directory for thumbs
                var thumbFilePath = mStorageref!!.child("profile_images")
                        .child("thumbs")
                        .child("$userId.jpg")

                filePath.putFile(resultUri)
                        .addOnCompleteListener{
                            task: Task<UploadTask.TaskSnapshot> ->
                            if (task.isSuccessful) {

                                //getting the picture url
                                var downloadUrl = task.result.downloadUrl.toString()

                                //upload task
                                var uploadTask: UploadTask = thumbFilePath
                                        .putBytes(thumbByteArray)

                                uploadTask.addOnCompleteListener{
                                    task: Task<UploadTask.TaskSnapshot> ->
                                    var thumbUrl = task.result.downloadUrl.toString()

                                    if (task.isSuccessful) {

                                        var updateObj = HashMap<String, Any>()
                                        updateObj.put("image", downloadUrl)
                                        updateObj.put("thumb_image", thumbUrl)

                                        //saving profile image
                                        mDatabase!!.updateChildren(updateObj)
                                                .addOnCompleteListener {
                                                    task: Task<Void> ->
                                                    if (task.isSuccessful) {
                                                        userProfileLayout1.animate().alpha(1f)
                                                        Toast.makeText(this, "Profile image saved!", Toast.LENGTH_LONG).show()
                                                    } else {}
                                                }

                                    } else {}
                                }

                            }
                        }
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
