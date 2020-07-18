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
import kotlinx.android.synthetic.main.activity_admin_announcements.*

class AdminAnnouncements : AppCompatActivity() {
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mFirebaseAdapter: FirebaseRecyclerAdapter<Posts, PostsViewHolder>? = null
    var mDatabase: DatabaseReference? = null

    var mFirebaseDatabaseX: DatabaseReference? = null
    var mFirebaseUser: FirebaseUser? = null
    var currentUserId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_announcements)

        adminrecyclerviewid.setHasFixedSize(true)
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true

        mDatabase = FirebaseDatabase.getInstance().reference.child("PostDescription")

        mFirebaseDatabaseX = FirebaseDatabase.getInstance().reference.child("Users")
        mFirebaseUser = FirebaseAuth.getInstance().currentUser
        currentUserId = mFirebaseUser?.uid


        adminrecyclerviewid.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                adminScrollPostId.visibility = View.GONE
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }
        })

        mDatabase!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                adminrecyclerviewid.scrollToPosition(adminrecyclerviewid.adapter.itemCount)
                adminScrollPostId.visibility = View.VISIBLE
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


        viewUserListId.setOnClickListener {
            startActivity(Intent(this, UserList::class.java))
        }

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

            }
        }
        adminrecyclerviewid.layoutManager = mLinearLayoutManager
        adminrecyclerviewid.adapter = mFirebaseAdapter
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
        menuInflater.inflate(R.menu.add_post, menu)

        var shareItem: MenuItem = menu!!.findItem(R.id.addIcon)


        if (currentUserId != null) {
            shareItem.setVisible(false)
        } else {
            shareItem.setVisible(true)
        }


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)


        if (item != null) {
            if (item.itemId == R.id.addIcon) {
                //take user to add post activity
                startActivity(Intent(this, AddPost::class.java))
            }
        }

        return true
    }

}
