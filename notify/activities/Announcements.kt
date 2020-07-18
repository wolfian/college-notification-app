package com.ionwayinc.project.notify.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.ionwayinc.project.notify.R
import com.ionwayinc.project.notify.models.Posts
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_announcements.*
import kotlinx.android.synthetic.main.activity_interaction.*
import kotlinx.android.synthetic.main.post_row.*

class Announcements : AppCompatActivity() {
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mFirebaseAdapter: FirebaseRecyclerAdapter<Posts, PostsViewHolder>? = null
    var mDatabase: DatabaseReference? = null

    var mFirebaseDatabaseX: DatabaseReference? = null
    var mFirebaseUser: FirebaseUser? = null
    var currentUserId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcements)

        recyclerviewid.setHasFixedSize(true)
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true

        mDatabase = FirebaseDatabase.getInstance().reference.child("PostDescription")

        mFirebaseDatabaseX = FirebaseDatabase.getInstance().reference.child("Users")
        mFirebaseUser = FirebaseAuth.getInstance().currentUser
        currentUserId = mFirebaseUser?.uid


        recyclerviewid.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                scrollPostId.visibility = View.GONE
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }
        })

        mDatabase!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                recyclerviewid.scrollToPosition(recyclerviewid.adapter.itemCount)
                scrollPostId.visibility = View.VISIBLE
            }

            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot?) {

            }
        })

    }

    override fun onStart() {
        super.onStart()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Posts, PostsViewHolder>(
                Posts::class.java,
                R.layout.post_row,
                PostsViewHolder::class.java,
                mDatabase
        ){
            override fun populateViewHolder(viewHolder: PostsViewHolder?, model: Posts?, position: Int) {

                if (model != null) {
                    viewHolder!!.setDescription(model)
                    viewHolder!!.setImage(model)
                    viewHolder!!.setTime(model)
                }

                viewHolder!!.post_image!!.setOnClickListener {
                    //Toast.makeText(this@Announcements, model!!.image, Toast.LENGTH_LONG).show()
                    var intent = Intent(this@Announcements, PostImage::class.java)
                    intent.putExtra("image", model!!.image).toString()
                    startActivity(intent)
                }

            }
        }
        recyclerviewid.layoutManager = mLinearLayoutManager
        recyclerviewid.adapter = mFirebaseAdapter
    }

    class PostsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var post_description: TextView? = null
        var post_image: ImageView? = null
        var post_time: TextView? = null

        fun setDescription(model: Posts){
            post_description = itemView.findViewById(R.id.textdescription)
            post_description!!.text = model.desc
        }

        fun setImage(model: Posts){
            post_image = itemView.findViewById(R.id.post_image_id)
            Picasso.get()
                    .load(model.image)
                    .into(post_image)
        }

        fun setTime(model: Posts){
            post_time = itemView.findViewById(R.id.posttimeid)
            post_time!!.text = model.time
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

        var id = android.os.Process.myPid()
        android.os.Process.killProcess(id)

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this, Dashboard::class.java))

        var id = android.os.Process.myPid()
        android.os.Process.killProcess(id)
    }

}
