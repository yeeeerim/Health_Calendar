package com.example.healthcalendar


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import calenderClass.Routine
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_routine.*

class RoutineActivity : AppCompatActivity() {
    var selectedPart: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)
        //캘린더에서 받아온 날짜
        var date :String=""
        if(intent.hasExtra("date")){
            date= intent.getStringExtra("date").toString()
        }else{
            Toast.makeText(this,"날짜 못받아옴.", Toast.LENGTH_SHORT).show()}
        //db연결
        val database :FirebaseDatabase= FirebaseDatabase.getInstance()
        val dbRef : DatabaseReference = database
            .getReference(FirebaseAuth.getInstance().currentUser!!.uid).child(date).child("Routine")


        val RoutineList = arrayListOf<Routine>()
        routine_recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        routine_recyclerView.setHasFixedSize(true)

        routine_recyclerView.adapter=RoutineAdapter(RoutineList)


        val spinner = findViewById<View>(R.id.bodyPartSpinner) as Spinner
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedPart = parent.getItemIdAtPosition(position).toString()
            }

            //아무것도 선택 안한 경우.
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        //추가버튼 클릭시
        btn_routine_added.setOnClickListener{
            var name = workOutName.text.toString()
            var set=workOutset.text.toString().toInt()
            var num=workOutset.text.toString().toInt()
            var bodypart=spinner.selectedItem.toString()
            RoutineList.add(Routine(name,set,num,bodypart))
            routine_recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            routine_recyclerView.setHasFixedSize(true)

        }
        //적용버튼 클릭시
        btn_routine_apply.setOnClickListener {
            //DB에 집어넣음.
            dbRef.setValue(RoutineList)
            onBackPressed()
        }
        //취소버튼 클릭시
        btn_routine_cancel.setOnClickListener { onBackPressed() }
    }
}