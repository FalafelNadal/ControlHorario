package net.azarquiel.pruebafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{
    BASIC
}

class HomeActivity : AppCompatActivity() {
    private lateinit var logOutButton: Button
    private lateinit var emailTextView: TextView
    private lateinit var providerTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?: "", provider ?: "")


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
}