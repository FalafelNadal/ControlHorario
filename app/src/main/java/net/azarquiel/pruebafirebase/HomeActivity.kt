package net.azarquiel.pruebafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ProviderType{
    BASIC
}

class HomeActivity : AppCompatActivity() {
    private lateinit var logOutButton: Button
    private lateinit var emailTextView: TextView
    private lateinit var providerTextView: TextView
    private lateinit var startTextView: TextView
    private lateinit var endTextView: TextView
    private lateinit var startButton: Button
    private lateinit var endButton: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?: "", provider ?: "")

        startTextView = findViewById(R.id.startTextView)
        endTextView = findViewById(R.id.endTextView)
        startButton = findViewById(R.id.startButton)
        endButton = findViewById(R.id.endButton)

        setupTimeButtons()

    }
    private fun setup(email: String, provider: String){
        title = "Inicio"
        emailTextView = findViewById(R.id.emailTextView)
        providerTextView = findViewById(R.id.providerTextView)

        emailTextView.text = email
        providerTextView.text = provider

        logOutButton = findViewById(R.id.logOutButton)
 
        logOutButton.setOnClickListener{
        FirebaseAuth.getInstance().signOut()
        finish()
        }

    }
    private fun setupTimeButtons() {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        startButton.setOnClickListener {
            val currentTime = timeFormat.format(Date())
            startTextView.text = "Hora de inicio: $currentTime"
            endTextView.text = "Hora de fin"
        }

        endButton.setOnClickListener {
            val endTime = timeFormat.format(Date())
            endTextView.text = "Hora de fin: $endTime"
        }
    }
}