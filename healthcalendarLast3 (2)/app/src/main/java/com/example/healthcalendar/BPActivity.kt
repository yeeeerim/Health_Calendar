package com.example.healthcalendar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import calenderClass.BodyProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_bodyprofile.*
import com.google.firebase.database.DatabaseReference

/*
* uid
*   ㄴDayIAm
*          ㄴArrayList<Food>
*
*          ㄴBodyProfile
*          ㄴArrayList<Routine>
* */
class BPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bodyprofile)
        //캘린더액티비티에서 선택한 날짜값 가져옴.
        var date :String=""
        if(intent.hasExtra("date")){
            date= intent.getStringExtra("date").toString()
        }else{
            Toast.makeText(this,"날짜 못받아옴.",Toast.LENGTH_SHORT).show()}

        //파이어베이스 DB연동
        val database :FirebaseDatabase= FirebaseDatabase.getInstance()
        val dbRef : DatabaseReference = database
            .getReference(FirebaseAuth.getInstance().currentUser!!.uid).child(date).child("BodyProfile")

        //적용버튼
        btn_bpa_apply.setOnClickListener {
            var height=height.text.toString().toDouble()
            var weight=weight.text.toString().toDouble()
            var muscle_kg=muscle_kg.text.toString().toDouble()
            var fat_kg=fat_kg.text.toString().toDouble()



            //DB에 집어넣음.
            dbRef.setValue(BodyProfile(weight,height,muscle_kg,fat_kg))
            // AddOnActivity로 넘어감.
            onBackPressed()
        }
        //취소버튼
        btn_bpa_cancel.setOnClickListener { onBackPressed() }



        }
    }
