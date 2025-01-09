package es.guillearana.proyecto

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class ExplicacionActivitySopa : AppCompatActivity() {

    private lateinit var btnPlayPause: Button
    private lateinit var btnRestart: Button
    private lateinit var btnNextActivity: Button
    private lateinit var seekBar: SeekBar
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTotalTime: TextView

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explicacion1)

        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnRestart = findViewById(R.id.btnRestart)
        btnNextActivity = findViewById(R.id.btnNextActivity)
        seekBar = findViewById(R.id.seekBar)
        tvCurrentTime = findViewById(R.id.tvCurrentTime)
        tvTotalTime = findViewById(R.id.tvTotalTime)

        // Cargar un archivo de audio fijo desde la carpeta `res/raw`
        mediaPlayer = MediaPlayer.create(this, R.raw.lanperaudio) // Asegúrate de tener un archivo `audio.mp3` en la carpeta `res/raw`

        mediaPlayer?.setOnPreparedListener {
            seekBar.max = mediaPlayer!!.duration
            tvTotalTime.text = formatTime(mediaPlayer!!.duration)
        }
        mediaPlayer?.setOnCompletionListener {
            btnNextActivity.isEnabled = true // Habilitar el botón al terminar el audio
            seekBar.progress = seekBar.max
            tvCurrentTime.text = formatTime(seekBar.max)
            isPlaying = false
            btnPlayPause.text = "Reproducir"
            handler.removeCallbacksAndMessages(null) // Detener actualizaciones de la barra
        }


        btnPlayPause.setOnClickListener { playPauseAudio() }
        btnRestart.setOnClickListener { restartAudio() }
        btnNextActivity.setOnClickListener { goToNextActivity() }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer?.seekTo(progress)
                    tvCurrentTime.text = formatTime(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun playPauseAudio() {
        if (mediaPlayer == null) return

        if (isPlaying) {
            mediaPlayer?.pause()
            btnPlayPause.text = "Reproducir"
        } else {
            mediaPlayer?.start()
            btnPlayPause.text = "Pausar"
            updateSeekBar() // Comenzar actualizaciones de la barra
        }
        isPlaying = !isPlaying
    }


    private fun restartAudio() {
        if (mediaPlayer != null) {
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
            btnPlayPause.text = "Pausar"
            isPlaying = true
            updateSeekBar()
        }
    }

    private fun updateSeekBar() {
        if (mediaPlayer == null ) return

        // Actualiza la posición del SeekBar y los tiempos
        seekBar.progress = mediaPlayer!!.currentPosition
        tvCurrentTime.text = formatTime(mediaPlayer!!.currentPosition)

        // Llama a este método de nuevo después de un breve retraso
        handler.postDelayed({ updateSeekBar() }, 500)
    }


    private fun formatTime(milliseconds: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds.toLong()) % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    private fun goToNextActivity() {
        val intent = Intent(this, SopaDeLetrasActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        handler.removeCallbacksAndMessages(null)
    }
}

