package com.example.myapplication_1124

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication_1124.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    data class User(
        val email: String = "",
        val password: String = ""
    ){
        constructor() : this("", "")
    }

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var textViewLogin : TextView

    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        registerButton = findViewById(R.id.buttonRegister)
        textViewLogin = findViewById(R.id.textViewLogin)

        // Firebase 초기화
        database = FirebaseDatabase.getInstance()
        usersRef = database.reference.child("users")

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // 새로운 사용자 객체 생성
            val user = User(email, password)

            // 사용자 데이터를 데이터베이스에 저장
            val userId = usersRef.push().key
            userId?.let {
                usersRef.child(userId).setValue(user)
                    .addOnSuccessListener {
                        Log.d(TAG, "사용자 등록 성공!")
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish() // 현재 액티비티 종료
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "사용자 등록 오류: ${e.message}")
                    }
            }
        }
        textViewLogin.setOnClickListener {
            // 로그인 텍스트 클릭 시 처리
            // 로그인 화면으로 이동하는 로직을 구현
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}