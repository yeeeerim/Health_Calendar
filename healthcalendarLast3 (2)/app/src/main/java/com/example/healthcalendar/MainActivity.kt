package com.example.healthcalendar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.community.ViewListActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var userid : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movecalendar.setOnClickListener {
            val intent = Intent(this,fragument_calander::class.java)
            startActivity(intent)
        }

        moveprofile.setOnClickListener {
            val intent = Intent(this,fragment_profile::class.java)
            startActivity(intent)
        }
        movecommunity.setOnClickListener {
            val intent = Intent(this,ViewListActivity::class.java)
            startActivity(intent)
        }



    }

    var clicktime:Long = 0
    override fun onBackPressed() {
        if(System.currentTimeMillis() - clicktime >=3000 ) {
            clicktime = System.currentTimeMillis()
            Toast.makeText(this,"정말로 종료하시겠습니까?", Toast.LENGTH_SHORT).show()
        } else {
            moveTaskToBack(true)
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }


}