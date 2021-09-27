package com.example.healthcalendar

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*


class signupActivity : AppCompatActivity() {

    //private var mAuth : FirebaseAuth? = null
    private lateinit var auth: FirebaseAuth
    private val TAG : String = "CreateAccount"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()


        createuser.setOnClickListener {

            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("Users")



            if (createemail.text.toString().isEmpty() || createpassword.text.toString().isEmpty() || passwordcheck.text.toString().length < 0){
                Toast.makeText(this, "email 혹은 password가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
            else if(!isEmailValid(createemail.text.toString())){
                Toast.makeText(this, "아이디는 이메일 형식으로 작성해야 합니다.", Toast.LENGTH_SHORT).show()
            }
            else if (createpassword.text.toString().length < 8){
                Toast.makeText(this, "비밀번호는 8자 이상이여야 합니다.", Toast.LENGTH_SHORT).show()
            }
            else if (createpassword.text.toString().length > 15){
                Toast.makeText(this, "비밀번호는 15자 이하여야 합니다.", Toast.LENGTH_SHORT).show()
            }
            else if(createpassword.text.toString() != passwordcheck.text.toString()) {
                Toast.makeText(this, "password가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
            else if(signupnickname.text.toString().length < 2){
                Toast.makeText(this, "닉네임은 최소 2자 이상 작성해야 합니다.", Toast.LENGTH_SHORT).show()
            }
            else if(signupage.text.toString().toInt() > 100 || signupage.text.toString().toInt() < 1 ){
                Toast.makeText(this, "나이를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show()
            }
            else
             {
                auth.createUserWithEmailAndPassword(
                    createemail.text.toString(),
                    createpassword.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                            val uid = user?.uid
                            val nickname: String = signupnickname.getText().toString().trim()
                            val age: String = signupage.getText().toString().trim()
                            val hashMap: HashMap<Any, String?> = HashMap()
                            hashMap["nickname"] = nickname
                            hashMap["age"] = age
                            val database = FirebaseDatabase.getInstance()
                            val reference = database.getReference("Users")
                            reference.child(uid!!).setValue(hashMap)
                            user.sendEmailVerification()
                            Toast.makeText(
                                this, "이메일인증을 위해 이메일 확인을 해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                this, "회원가입 실패",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        backloginbtn.setOnClickListener {
            onBackPressed()
        }



    }
    private fun isEmailValid(email : String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}