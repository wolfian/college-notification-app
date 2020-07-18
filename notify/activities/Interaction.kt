package com.ionwayinc.project.notify.activities

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.ionwayinc.project.notify.R
import com.ionwayinc.project.notify.models.FriendlyMessage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_interaction.*
import android.text.Spannable
import android.R.attr.name
import android.content.Intent
import android.text.style.StyleSpan
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.item_message.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ChildEventListener
import com.ionwayinc.project.notify.R.id.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class Interaction : AppCompatActivity() {

    var mFirebaseDatabaseRef1: DatabaseReference? = null
    var mFirebaseDatabaseRef2: DatabaseReference? = null
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mFirebaseUser: FirebaseUser? = null
    var mDatabase: DatabaseReference? = null
    var mDatabaseKey: String? = null
    var mFirebaseAdapter: FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interaction)


        mFirebaseDatabaseRef1 = FirebaseDatabase.getInstance().reference.child("Messages")
        mFirebaseDatabaseRef2 = FirebaseDatabase.getInstance().reference.child("Users")


        messageRecyclerView.setHasFixedSize(true)
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true

        mFirebaseUser = FirebaseAuth.getInstance().currentUser

        var displayName:String? = null
        var imageUrl:String? = null

        var currentUserId = mFirebaseUser!!.uid

        mFirebaseDatabaseRef2!!.child(currentUserId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(data: DataSnapshot?) {
                displayName = data!!.child("display_name").value.toString()
                imageUrl = data!!.child("thumb_image").value.toString()
            }

            override fun onCancelled(error: DatabaseError?) {

            }
        })


        //moved to onStart()


        messageRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                scrollTextId.visibility = View.GONE
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }
        })


        mFirebaseDatabaseRef1!!.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                messageRecyclerView.scrollToPosition(messageRecyclerView.adapter.itemCount)
                scrollTextId.visibility = View.VISIBLE
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


        sendMessageButtonId.setOnClickListener {


            var currentTime = LocalDateTime.now()
            var currentDate = LocalDate.now()
            var timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
            var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            var formattedTime = currentTime.format(timeFormatter)
            var formattedDate = currentTime.format(dateFormatter)

            var displayNameValue = displayName.toString().trim()
            var imageUrlValue = imageUrl.toString().trim()
            var messageValue = "\n$displayNameValue ->  ${messageEditId.text.toString().trim()}"
            var dateTimeValue = "$formattedTime; $formattedDate"


            if(TextUtils.isEmpty(messageEditId.text)){
                Toast.makeText(this, "Null message", Toast.LENGTH_LONG).show()
            }

            if(!TextUtils.isEmpty(messageEditId.text)){


                var newPost: DatabaseReference = mFirebaseDatabaseRef1!!.push()
                newPost.child("text").setValue(messageValue)
                newPost.child("time").setValue(dateTimeValue)

                }


                messageEditId.setText("")


            }
        }

    override fun onStart() {
        super.onStart()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<FriendlyMessage, MessageViewHolder>(
                FriendlyMessage::class.java,
                R.layout.item_message,
                MessageViewHolder::class.java,
                mFirebaseDatabaseRef1){

            override fun populateViewHolder(viewHolder: MessageViewHolder?, model: FriendlyMessage?, position: Int) {

                if (model != null) {
                    viewHolder!!.settext(model)
                    viewHolder!!.settime(model)

                }

            }
        }
        messageRecyclerView.layoutManager = mLinearLayoutManager
        messageRecyclerView.adapter = mFirebaseAdapter

    }


    class MessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var messagetext: TextView? = null
        var messagetime: TextView? = null
        var messagedisplayname: TextView? = null
        var messageprofileimage: ImageView? = null


        fun settext(model: FriendlyMessage){
            messagetext = itemView.findViewById(R.id.messageText)
            messagetext!!.text = model.text
        }


        fun settime(model: FriendlyMessage){
            messagetime = itemView.findViewById(messageTime)
            messagetime!!.text = model.time
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
