package com.example.myfirstapplication

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myfirstapplication.databinding.ActivityMainBinding



class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita o modo de exibição de ponta a ponta (edge-to-edge)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Define o layout da atividade
        setContentView(binding.root)
        
        // Configura o ouvinte para ajustar o preenchimento (padding) da view de acordo com as barras do sistema (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Identifica o elemento de entrada de texto (EditText) para o e-mail pelo seu ID
//        val editTextEmail: EditText = findViewById<EditText>(R.id.edittext_email)
//        val editTextPassword: EditText = findViewById<EditText>(R.id.edittext_password)
//        val buttonLogin: Button = findViewById<Button>(R.id.button_login)
//        val buttonRegister: Button = findViewById<Button>(R.id.button_register)






    }
}
