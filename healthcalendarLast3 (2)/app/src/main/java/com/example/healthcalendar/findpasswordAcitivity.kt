package com.example.healthcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_findpassword_acitivity.*

class findpasswordAcitivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findpassword_acitivity)

        findpasswordcanclebtn.setOnClickListener {
            finish()
        }

        emailcheckbutton.setOnClickListener{
            emailcheckbutton.text = "재전송"
            FirebaseAuth.getInstance().sendPasswordResetEmail(findpasswordemail.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this,"이메일을 확인하여 비밀번호를 변경하세요.",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}