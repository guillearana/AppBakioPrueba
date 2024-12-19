package es.guillearana.proyecto

import android.os.Bundle
import android.widget.ImageView
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Encuentra el ImageView en el que quieres mostrar la animación
        val imageView = findViewById<ImageView>(R.id.imageView2)

        // Cargar la animación desde el archivo XML
        val animationDrawable = resources.getDrawable(R.drawable.sonrisa) as AnimationDrawable

        // Establecer la animación en el ImageView
        imageView.setImageDrawable(animationDrawable)

        // Iniciar la animación
        animationDrawable.start()
    }
}


