package com.ionwayinc.project.notify.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ionwayinc.project.notify.R
import com.ionwayinc.project.notify.models.Posts
import com.ionwayinc.project.notify.models.Users
import kotlinx.android.synthetic.main.activity_announcements.*
import kotlinx.android.synthetic.main.activity_user_list.*

class UserList : AppCompatActivity() {
    var mUserDatabase: DatabaseReference? = null
    var mLinearLayoutManager: LinearLayoutManager? = null
    var mFirebaseAdapter: FirebaseRecyclerAdapter<Users, UserList.ViewHolder>? = null
    var mFirebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        userListRecyclerViewId.setHasFixedSize(true)
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true

        mUserDatabase = FirebaseDatabase.getInstance().reference.child("Users")
    }

    override fun onStart() {
        super.onStart()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Users, ViewHolder>(
                Users::class.java,
                R.layout.user_list,
                ViewHolder::class.java,
                mUserDatabase
        ){
            override fun populateViewHolder(viewHolder: ViewHolder?, model: Users?, position: Int) {

                if (model != null) {
                    viewHolder!!.setName(model)
                    viewHolder!!.setEmail(model)
                    viewHolder!!.setRegistration(model)
                }

            }
        }
        userListRecyclerViewId.layoutManager = mLinearLayoutManager
        userListRecyclerViewId.adapter = mFirebaseAdapter
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var userDisplayNameTxt: TextView? = null
        var userEmailAddressTxt: TextView? = null
        var userRegistrationIdTxt: TextView? = null


        fun setName(model: Users){
            userDisplayNameTxt = itemView.findViewById(R.id.userListDisplayNameTextId)
            userDisplayNameTxt!!.text = model.display_name
        }

        fun setEmail(model: Users){
            userEmailAddressTxt = itemView.findViewById(R.id.userListEmailAddressTextId)
            userEmailAddressTxt!!.text = model.email_address        }

        fun setRegistration(model: Users){
            userRegistrationIdTxt = itemView.findViewById(R.id.userListRegistrationIdTextId)
            userRegistrationIdTxt!!.text = model.registration_id        }

    }
}
