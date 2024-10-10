package net.azarquiel.pruebafirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ProviderType {
    BASIC
}

class HomeActivity : AppCompatActivity() {
    private lateinit var logOutButton: Button
    private lateinit var emailTextView: TextView
    private lateinit var providerTextView: TextView
    private lateinit var startTextView: TextView
    private lateinit var endTextView: TextView
    private lateinit var countTextView: TextView
    private lateinit var startButton: Button
    private lateinit var endButton: Button
    private lateinit var stopButton: Button
    private lateinit var deleteButton: Button
    private var currentTime: String? = null
    private var endTime: String? = null
    private var timeDifference: String? = null
    private val db = FirebaseFirestore.getInstance()
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?: "", provider ?: "")

        startTextView = findViewById(R.id.startTextView)
        endTextView = findViewById(R.id.endTextView)
        countTextView = findViewById(R.id.countTextView)
        startButton = findViewById(R.id.startButton)
        endButton = findViewById(R.id.endButton)
        stopButton = findViewById(R.id.stopButton)
        deleteButton = findViewById(R.id.deleteButton)

        setupTimeButtons()
    }

    private fun setup(email: String, provider: String) {
        title = "Inicio"
        emailTextView = findViewById(R.id.emailTextView)
        providerTextView = findViewById(R.id.providerTextView)

        emailTextView.text = email
        providerTextView.text = provider

        logOutButton = findViewById(R.id.logOutButton)

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }
    }

    private fun setupTimeButtons() {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        startButton.setOnClickListener {
            if (currentTime == null) {
                currentTime = timeFormat.format(Date())
                startTextView.text = "Hora de inicio: $currentTime"
                endTextView.text = "Hora de fin"
            } else {
                Toast.makeText(this, "Ya hay una hora de inicio", Toast.LENGTH_SHORT).show()
            }
        }

        endButton.setOnClickListener {
            if (endTime == null) {
                if (currentTime == null) {
                    Toast.makeText(this, "No hay hora de inicio", Toast.LENGTH_SHORT).show()
                } else {

                    endTime = timeFormat.format(Date())
                    endTextView.text = "Hora de fin: $endTime"

                    val startDate = timeFormat.parse(currentTime!!)
                    val endDate = timeFormat.parse(endTime!!)

                    if (startDate != null && endDate != null) {
                        val differenceInMillis = endDate.time - startDate.time

                        val hours = (differenceInMillis / (1000 * 60 * 60)).toInt()
                        val minutes = ((differenceInMillis / (1000 * 60)) % 60).toInt()
                        val seconds = ((differenceInMillis / 1000) % 60).toInt()

                        val timeDifference = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                        countTextView.text = "Suma de horas: $timeDifference"

                        if (email != null) {
                            val sessionData = hashMapOf(
                                "fecha de inicio" to startTextView.text.toString(),
                                "fecha de salida" to endTextView.text.toString(),
                                "suma de horas" to timeDifference
                            )
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault())
                            val currentDateAndTime = dateFormat.format(Date())

                            val sessionId = "session_$currentDateAndTime"

                            db.collection("users").document(email!!)
                                .collection("sessions").document(sessionId).set(sessionData)
                                .addOnSuccessListener{
                                    Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "Email no disponible", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Error en las fechas", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Ya hay hora de salida", Toast.LENGTH_SHORT).show()
            }
        }




        stopButton.setOnClickListener {

        }

        deleteButton.setOnClickListener {
            currentTime = null
            endTime = null
            timeDifference = null
            startTextView.text = "Hora de inicio"
            endTextView.text = "Hora de fin"
            countTextView.text = "Suma de horas"
        }
    }
}
