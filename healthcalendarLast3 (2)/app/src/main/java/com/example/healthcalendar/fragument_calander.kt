package com.example.healthcalendar

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.calander_layout.*
import kotlinx.android.synthetic.main.profile_layout.*
import java.util.*

class fragument_calander: AppCompatActivity(){

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calander_layout)


        val time=System.currentTimeMillis()
        val dateFormat= SimpleDateFormat("yyyyMMdd")
        //날짜를 선택하지 않고 데이터 입력시 선택한 날짜를 현재 날짜로.
        var dateSelected=dateFormat.format(Date(time))
        //DB연결
        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val dbRef : DatabaseReference = database
            .getReference(FirebaseAuth.getInstance().currentUser!!.uid)//.child(dateSelected)


        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            calendar_diet_text.setText("")
            calendar_routine_text.setText("")

            text_calander.visibility=View.VISIBLE
            text_calander.setText(String.format("%d - %d -%d", year, month + 1, dayOfMonth))
            dateSelected=String.format("%d%d%d", year, month + 1, dayOfMonth)

            dbRef.addValueEventListener(object : ValueEventListener
            {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    //신체정보 가져오기+화면에 띄움.
                    if(snapshot.child(dateSelected).child("BodyProfile").value!=null) {
                        var bodyProfile = snapshot.child(dateSelected)
                            .child("BodyProfile").value as Map<String, Any>
                        calendar_bodyprofile_text.text =
                            "체중 : " + bodyProfile["weight"].toString() + "\n" +
                                    "키 : " + bodyProfile["cm"].toString() + "\n" +
                                    "골격근량 : " + bodyProfile["muscle_kg"].toString() + "\n" +
                                    "체지방량 : " + bodyProfile["fat_kg"].toString()
                    }else{calendar_bodyprofile_text.text =""}

                    //식단정보 가져오기+화면에 띄움.
                    if(snapshot.child(dateSelected).child("Diet").value!=null){
                        for(i in 0..(snapshot.child(dateSelected).child("Diet").childrenCount-1))
                        {var diet=snapshot.child(dateSelected).child("Diet").child(i.toString()).value as Map<String,Any>
                            calendar_diet_text.append(
                                "음식이름 : "+diet["name"].toString()+"\n"+
                                        "100g당 칼로리 : "+diet["cal_per_100g"].toString()+"\n"+
                                        "개수 : "+diet["how_many"].toString()+"\n\n")}
                    }else{calendar_diet_text.text=""}
                    //운동정보 가져오기+화면에 띄움.
                    if(snapshot.child(dateSelected).child("Routine").value!=null){
                        for(i in 0..(snapshot.child(dateSelected).child("Routine").childrenCount-1))
                        {var routine=snapshot.child(dateSelected).child("Routine").child(i.toString()).value as Map<String,Any>
                            calendar_routine_text.append(
                                "운동이름 : "+routine["name"].toString()+"\n"+
                                        "세트 : "+routine["set"].toString()+"\n"+
                                        "개수 : "+routine["num"].toString()+"\n"+
                                        "운동부위 : " +routine["bodyPart"].toString()+"\n\n")}
                    }else{calendar_routine_text.text=""}
                }
            })
        }



        btn_plus.setOnClickListener{
            val nextIntent= Intent(this, addOnActivity::class.java)
            nextIntent.putExtra("date", dateSelected)
            startActivity(nextIntent)
        }
        btn_graph.setOnClickListener{
            val nextIntent= Intent(this, GraphActivity::class.java)
            nextIntent.putExtra("date", dateSelected)
            startActivity(nextIntent)
        }
        }
    }
