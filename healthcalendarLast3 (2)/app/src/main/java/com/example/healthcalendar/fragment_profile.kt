package com.example.healthcalendar

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.profile_layout.*


class fragment_profile: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_layout)

        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("Users").child(user?.uid.toString())
        val referencer = database.getReference(user?.uid.toString())


        referencer.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var day = 100
                for (Data in snapshot.children) {
                    if (day < Data.key.toString().toInt()) {
                        day = Data.key.toString().toInt()
                    }
                }

                for (Data in snapshot.child(day.toString()).child("BodyProfile").children) {
                    if (Data.key.toString() == "cm")
                        profileheight.text = Data.value.toString()
                    if (Data.key.toString() == "fat_kg")
                        profileweight.text = Data.value.toString()

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        )


        reference.child("nickname").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                profilenickname.text = p0.value.toString()
            }
        })
        reference.child("age").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                profileage.text = p0.value.toString()
            }
        })


        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginAcivity::class.java)
            startActivity(intent)
        }

        btn_fix.setOnClickListener {
            profileweight.visibility = View.GONE
            profilenickname.visibility = View.GONE
            profileheight.visibility = View.GONE
            profileage.visibility = View.GONE
            profileagefix.visibility = View.VISIBLE
            profilenicknamefix.visibility = View.VISIBLE
            btn_fix.visibility = View.GONE
            btn_fixconfirm.visibility = View.VISIBLE
            btn_fixcancle.visibility = View.VISIBLE
            btn_signout.visibility = View.GONE
            btn_logout.visibility = View.GONE
            logouttext.visibility = View.GONE
            signouttext.visibility = View.GONE
            distext.visibility = View.GONE
            distext2.visibility = View.GONE
        }

        btn_fixconfirm.setOnClickListener {
            reference.child("nickname").setValue(profilenicknamefix.text.toString())
            reference.child("age").setValue(profileagefix.text.toString())
            profilenickname.text = profilenicknamefix.text.toString()
            profileage.text = profileagefix.text.toString()
            profileweight.visibility = View.VISIBLE
            profilenickname.visibility = View.VISIBLE
            profileheight.visibility = View.VISIBLE
            profileage.visibility = View.VISIBLE
            profileagefix.visibility = View.GONE
            profilenicknamefix.visibility = View.GONE
            btn_fix.visibility = View.VISIBLE
            btn_fixconfirm.visibility = View.GONE
            btn_fixcancle.visibility = View.GONE
            btn_signout.visibility = View.VISIBLE
            btn_logout.visibility = View.VISIBLE
            logouttext.visibility = View.VISIBLE
            signouttext.visibility = View.VISIBLE
            distext.visibility = View.VISIBLE
            distext2.visibility = View.VISIBLE
        }

        btn_fixcancle.setOnClickListener {
            profileweight.visibility = View.VISIBLE
            profilenickname.visibility = View.VISIBLE
            profileheight.visibility = View.VISIBLE
            profileage.visibility = View.VISIBLE
            profileagefix.visibility = View.GONE
            profilenicknamefix.visibility = View.GONE
            btn_fix.visibility = View.VISIBLE
            btn_fixconfirm.visibility = View.GONE
            btn_fixcancle.visibility = View.GONE
            btn_signout.visibility = View.VISIBLE
            btn_logout.visibility = View.VISIBLE
            logouttext.visibility = View.VISIBLE
            signouttext.visibility = View.VISIBLE
            distext.visibility = View.VISIBLE
            distext2.visibility = View.VISIBLE
        }

        btn_signout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.signout_dialog, null)
            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    FirebaseAuth.getInstance().currentUser?.delete()
                    moveTaskToBack(true)
                    finish()
                    android.os.Process.killProcess(android.os.Process.myPid())
                }
                .setNegativeButton("취소") { dialogInterface, i ->

                }
                .show()
        }
    }
}
