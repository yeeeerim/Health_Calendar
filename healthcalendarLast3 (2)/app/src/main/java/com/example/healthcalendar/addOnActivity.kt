package com.example.healthcalendar


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_on.*

class addOnActivity : AppCompatActivity() {
    var selectedDate: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_on)
        var date :String =""

       if(intent.hasExtra("date")){
           date= intent.getStringExtra("date").toString()
       }else{
           Toast.makeText(this,"날짜 못 받음",Toast.LENGTH_SHORT).show()}

        //리스너
        btnDiet.setOnClickListener {
            val intent = Intent(this, DietActivity::class.java)
            intent.putExtra("date",date)
            startActivity(intent)
        }
        btnBodyProfile.setOnClickListener {
            val intent = Intent(this, BPActivity::class.java)
            intent.putExtra("date",date)
            startActivity(intent)
        }
        btnRoutine.setOnClickListener {
            val intent = Intent(this, RoutineActivity::class.java)
            intent.putExtra("date",date)
            startActivity(intent)
        }
    }



}