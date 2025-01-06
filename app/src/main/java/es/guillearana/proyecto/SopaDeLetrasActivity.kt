package es.guillearana.proyecto

import android.graphics.Color
import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.view.Gravity
import android.widget.Button
import kotlin.math.abs
import kotlin.random.Random

class SopaDeLetrasActivity : AppCompatActivity() {

    private lateinit var buttonNext: Button
    private var lastSelectedRow: Int? = null
    private var lastSelectedCol: Int? = null
    private val foundLetterIndices = mutableSetOf<Int>()
    private val foundWords = mutableSetOf<String>()
    private val gridSize = 10

    // Lista de palabras que se deben encontrar
    private val words = listOf("LANPER", "UDALETXEA", "BAKIO", "DOLOZA", "ITSASOA", "ARRANTZA", "JAIAK")
    private val wordsToClues = mutableMapOf(
        "LANPER" to "..... ",
        "UDALETXEA" to "..... ",
        "BAKIO" to "..... ",
        "DOLOZA" to "..... ",
        "ITSASOA" to "..... ",
        "ARRANTZA" to "..... ",
        "JAIAK" to "..... "
    )
    private lateinit var gridLayout: GridLayout


    private var gridEnabled = true
    private lateinit var textViewClues: TextView
    private var selectedWord = StringBuilder()
    private lateinit var wordSearchLetters: Array<Array<Char>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sopa_de_letras)

        // Inicializa gridLayout
        gridLayout = findViewById(R.id.grid_sopa_de_letras)

        // Generar sopa de letras aleatoria
        wordSearchLetters = generateRandomWordSearch()

        // Configura la sopa de letras
        setupWordSearchGrid()
        textViewClues = findViewById(R.id.textViewClues)
        updateCluesDisplay()

        buttonNext = findViewById(R.id.button_next) // Asegúrate de inicializar buttonNext
        buttonNext.setOnClickListener {
            // Cambiar a la siguiente actividad
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun checkWord() {
        val currentWord = selectedWord.toString()

        if (currentWord in words && currentWord !in foundWords) {
            foundWords.add(currentWord)
            addFoundWordIndices(currentWord) // Agrega los índices de las letras de la palabra encontrada
            selectedWord.clear()
            updateCluesDisplay()

            if (foundWords.size == words.size) {
                buttonNext.isEnabled = true
            }
        }
    }

    private fun updateCluesDisplay() {
        val cluesText = wordsToClues.entries.joinToString("\n") { (word, clue) ->
            if (word in foundWords) word else clue
        }
        textViewClues.text = cluesText
    }

    private fun showCompletionMessage() {
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("¡Felicidades!")
            .setMessage("Has encontrado todas las palabras.")
            .setPositiveButton("OK") { _, _ ->
                // Acción tras el mensaje (opcional)
            }
            .create()
        dialog.show()
    }


    // Generar una sopa de letras aleatoria con las palabras dadas
    private fun generateRandomWordSearch(): Array<Array<Char>> {
        val grid = Array(gridSize) { Array(gridSize) { ' ' } }

        for (word in words) {
            var placed = false
            while (!placed) {
                val startRow = Random.nextInt(gridSize)
                val startCol = Random.nextInt(gridSize)
                val direction = Random.nextInt(8)

                placed = placeWordInGrid(grid, word, startRow, startCol, direction)
            }
        }

        // Rellenar los espacios vacíos con letras aleatorias
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                if (grid[i][j] == ' ') {
                    grid[i][j] = ('A'..'Z').random()
                }
            }
        }

        return grid
    }

    // Intentar colocar una palabra en la sopa de letras
    private fun placeWordInGrid(grid: Array<Array<Char>>, word: String, startRow: Int, startCol: Int, direction: Int): Boolean {
        val rowDirection = arrayOf(-1, -1, 0, 1, 1, 1, 0, -1)
        val colDirection = arrayOf(0, 1, 1, 1, 0, -1, -1, -1)

        var row = startRow
        var col = startCol

        // Verificar si la palabra cabe en la dirección dada
        for (char in word) {
            if (row !in 0 until gridSize || col !in 0 until gridSize || (grid[row][col] != ' ' && grid[row][col] != char)) {
                return false
            }
            row += rowDirection[direction]
            col += colDirection[direction]
        }

        // Colocar la palabra en la dirección dada
        row = startRow
        col = startCol
        for (char in word) {
            grid[row][col] = char
            row += rowDirection[direction]
            col += colDirection[direction]
        }

        return true
    }

    // Configurar la rejilla de la sopa de letras
    private fun setupWordSearchGrid() {
        val rows = wordSearchLetters.size
        val cols = wordSearchLetters[0].size

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val textView = TextView(this)
                textView.layoutParams = GridLayout.LayoutParams(
                    GridLayout.spec(i, 1f),
                    GridLayout.spec(j, 1f)
                ).apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    setMargins(4, 4, 4, 4)
                }
                textView.gravity = Gravity.CENTER
                textView.textSize = 24f
                textView.setTextColor(Color.BLACK) // Establece el color del texto a negro
                textView.text = wordSearchLetters[i][j].toString()
                textView.isClickable = true
                textView.isFocusable = true

                textView.setOnClickListener {
                    handleLetterClick(i, j)
                }
                gridLayout.addView(textView)
            }
        }
    }


    // Manejar la selección de letras
    private fun handleLetterClick(row: Int, col: Int) {
        val linearIndex = row * gridSize + col
        val textView = gridLayout.getChildAt(linearIndex) as TextView

        // Permitir selección sin importar si la letra ya se usó en palabras anteriores
        if (lastSelectedRow == null || isAdjacent(row, col, lastSelectedRow!!, lastSelectedCol!!)) {
            // Si no hay selección previa o la letra es adyacente a la última seleccionada
            if (textView.isSelected) {
                // Desmarcar la selección actual si ya está seleccionada
                textView.isSelected = false
                textView.setBackgroundColor(Color.TRANSPARENT)
                selectedWord.deleteCharAt(selectedWord.length - 1)
            } else {
                // Seleccionar la nueva letra
                textView.isSelected = true
                textView.setBackgroundColor(Color.YELLOW)
                selectedWord.append(textView.text)
                lastSelectedRow = row
                lastSelectedCol = col
            }
        } else {
            // Reiniciar la selección si la letra no es adyacente
            resetSelection()
            handleLetterClick(row, col) // Intenta seleccionar de nuevo
        }

        checkWord()
    }


    private fun addFoundWordIndices(word: String) {
        for (i in wordSearchLetters.indices) {
            for (j in 0 until wordSearchLetters[i].size - word.length + 1) {
                var found = true
                for (k in word.indices) {
                    if (wordSearchLetters[i][j + k] != word[k]) {
                        found = false
                        break
                    }
                }

                if (found) {
                    // Simplemente registra los índices como encontrados (sin deshabilitar las letras)
                    for (k in word.indices) {
                        val linearIndex = i * 10 + j + k
                        foundLetterIndices.add(linearIndex)
                    }
                    return
                }
            }
        }
    }


    // Verificar si una letra es adyacente a otra
    private fun isAdjacent(row: Int, col: Int, lastRow: Int, lastCol: Int): Boolean {
        return abs(row - lastRow) <= 1 && abs(col - lastCol) <= 1
    }

    // Reiniciar la selección de letras
    private fun resetSelection(resetColors: Boolean = true) {
        for (i in 0 until gridLayout.childCount) {
            val textView = gridLayout.getChildAt(i) as TextView
            if (resetColors) {
                textView.setBackgroundColor(Color.TRANSPARENT)
            }
            textView.isSelected = false
        }
        selectedWord.setLength(0)
        lastSelectedRow = null
        lastSelectedCol = null
    }



}


