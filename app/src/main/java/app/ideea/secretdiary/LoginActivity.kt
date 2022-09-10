package app.ideea.secretdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

const val PASSWORD = "1234"

class LoginActivity : AppCompatActivity() {

    private lateinit var etPin: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bindViews()

        btnLogin.setOnClickListener {
            if (etPin.text.toString() == PASSWORD) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // show error message
                etPin.error = "Wrong PIN!"
            }
        }
    }

    private fun bindViews() {
        etPin = findViewById(R.id.etPin)
        btnLogin = findViewById(R.id.btnLogin)
    }
}