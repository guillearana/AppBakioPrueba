package es.guillearana.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.graphics.drawable.AnimationDrawable
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Encuentra el ImageView en el que quieres mostrar la animación
        val imageView = findViewById<ImageView>(R.id.imageView2)

        val jugarButton: Button = findViewById(R.id.button)

        jugarButton.setOnClickListener {
            val intent = Intent(this, SopaDeLetrasActivity::class.java)
            startActivity(intent)
        }

        // Cargar la animación desde el archivo XML
        val animationDrawable = resources.getDrawable(R.drawable.sonrisa) as AnimationDrawable

        // Establecer la animación en el ImageView
        imageView.setImageDrawable(animationDrawable)

        // Iniciar la animación
        animationDrawable.start()
    }
}


