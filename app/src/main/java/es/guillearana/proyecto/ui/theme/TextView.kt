package es.guillearana.proyecto.ui.theme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet

class TextView(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {
        override fun onDraw(canvas: Canvas) {
            val paint = Paint()
            paint.color = Color.RED // Color del borde
            paint.style = Paint.Style.STROKE // Tipo de estilo de borde
            paint.strokeWidth = 4f // Grosor del borde

            // Dibuja el texto con borde
            val x = (width - paint.measureText(text.toString())) / 2
            val y = (height / 2 + (paint.textSize / 2))

            // Dibuja el borde alrededor del texto
            canvas.drawText(text.toString(), x, y, paint)

            // Luego dibuja el texto en el color normal
            paint.color = Color.BLACK // Color del texto
            paint.style = Paint.Style.FILL // Relleno del texto
            super.onDraw(canvas)
        }
    }