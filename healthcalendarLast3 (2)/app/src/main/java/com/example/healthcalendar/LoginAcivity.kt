package com.example.healthcalendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.community.LoadingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login_acivity.*


class LoginAcivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
 //   private val firebaseAuth: FirebaseAuth? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_acivity)

        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)

        auth = FirebaseAuth.getInstance()

        loginbutton.setOnClickListener {

            if (idtext.text.toString().length == 0 || passwordtext.text.toString().length == 0){
                errortext.text = "아이디 혹은 비밀번호가 입력되지 않았습니다."
                errortext.visibility = View.VISIBLE
            } else {
                auth.signInWithEmailAndPassword(
                    idtext.text.toString(),
                    passwordtext.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            if(user?.isEmailVerified == true)
                            updateUI(user)
                            else{
                                user?.sendEmailVerification()
                                val builder = AlertDialog.Builder(this)
                                val dialogView = layoutInflater.inflate(
                                    R.layout.emailcheckdialog,
                                    null
                                )
                                builder.setView(dialogView)
                                    .setPositiveButton("확인") { dialogInterface, i ->

                                    }
                                    .show()

                            }
                        } else {
                            errortext.text = "아이디 혹은 비밀번호가 잘못되었습니다."
                            errortext.visibility = View.VISIBLE
                            updateUI(null)
                        }
                    }
            }

        }




        signupbutton.setOnClickListener {
            val intent = Intent(this, signupActivity::class.java)
            startActivity(intent)
        }

        searchpasswordbutton.setOnClickListener {
            val intent = Intent(this, findpasswordAcitivity::class.java)
            startActivity(intent)
        }
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser?.isEmailVerified == true)
        updateUI(currentUser)
    }


    fun updateUI(cUser: FirebaseUser? = null){
        if(cUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    var clicktime:Long = 0
    override fun onBackPressed() {
        if(System.currentTimeMillis() - clicktime >=3000 ) {
            clicktime = System.currentTimeMillis()
            Toast.makeText(this, "정말로 종료하시겠습니까?", Toast.LENGTH_SHORT).show()
        } else {
            moveTaskToBack(true)
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }



}