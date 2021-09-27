package com.example.healthcalendar


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import calenderClass.BodyProfile
import calenderClass.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_bodyprofile.*
import kotlinx.android.synthetic.main.activity_diet.*


class DietActivity : AppCompatActivity() {
    //final int GET_GALLERY_IMAGE=10000;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet)
        //캘린더에서 선택한 날짜 받아옴
        var date :String=""
        if(intent.hasExtra("date")){
            date= intent.getStringExtra("date").toString()
            Toast.makeText(this,date,Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"날짜 못받아옴.",Toast.LENGTH_LONG).show()}
        //db연결
        val database :FirebaseDatabase= FirebaseDatabase.getInstance()
        val dbRef : DatabaseReference = database
            .getReference(FirebaseAuth.getInstance().currentUser!!.uid).child(date).child("Diet")
        //리사이클러블객체
        val FoodList = arrayListOf<Food>()

        //음식추가버튼을 눌렀을 때
        btnAddFood.setOnClickListener {
            var food_name=foodName.text.toString()
            var cal_per_100g=cal_per_100g.text.toString().toDouble()
            var food_num=how_many.text.toString().toDouble()
            FoodList.add(
                Food(
                    R.drawable.googleg_disabled_color_18,
                    food_name,
                    cal_per_100g,
                    food_num
                )
            )
            food_recycleView.layoutManager= LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
            food_recycleView.setHasFixedSize(true)
            food_recycleView.adapter=FoodAdapter(FoodList)

        }


        //적용버튼
        btn_diet_apply.setOnClickListener {
            //DB에 집어넣음.
            dbRef.setValue(FoodList)
            onBackPressed()
        }
        //취소버튼
        btn_diet_cancel.setOnClickListener {
            onBackPressed() //뒤로가기
        }
    }
}