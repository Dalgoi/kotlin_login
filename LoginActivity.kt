package com.example.myapplication_1124

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication_1124.R
import com.example.myapplication_1124.RegisterActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var textViewRegister: TextView

    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        textViewRegister = findViewById(R.id.textViewRegister)

        // Firebase 초기화
        database = FirebaseDatabase.getInstance()
        usersRef = database.reference.child("users")

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // 사용자 정보 확인
            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var isLoginSuccessful = false

                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(RegisterActivity.User::class.java)

                        if (user != null && user.email == email && user.password == password) {
                            isLoginSuccessful = true
                            break
                        }
                    }

                    if (isLoginSuccessful) {
                        Log.d(TAG, "로그인 성공!")
                        // 로그인 성공 처리
                    } else {
                        Log.d(TAG, "로그인 실패: 이메일 또는 비밀번호가 일치하지 않습니다.")
                        // 로그인 실패 처리
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "데이터베이스 오류: ${databaseError.message}")
                    // 오류 처리
                }
            })
        }
        textViewRegister.setOnClickListener {
            // 회원가입 텍스트 클릭 시 처리
            // 회원가입 화면으로 이동하는 로직을 구현
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}