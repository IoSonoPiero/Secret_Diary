package app.ideea.secretdiary

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.datetime.Clock
import java.text.SimpleDateFormat
import java.util.*

const val PREF_DIARY = "PREF_DIARY"
const val KEY_DIARY_TEXT = "KEY_DIARY_TEXT"

class MainActivity : AppCompatActivity() {

    private lateinit var etNewWriting: EditText
    private lateinit var btnSave: Button
    private lateinit var tvDiary: TextView
    private lateinit var alertDialogButton: Button

    private val noteList = mutableListOf<Note>()

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()

        sharedPreferences = getSharedPreferences(PREF_DIARY, Context.MODE_PRIVATE)
        loadDiary()

        val dialog: AlertDialog = createDialog()

        alertDialogButton.setOnClickListener {
            dialog.show()
        }

        btnSave.setOnClickListener {
            val messageToSave: String = etNewWriting.text.toString()

            if (messageToSave.isBlank()) {
                Toast.makeText(this, getString(R.string.blank_text), Toast.LENGTH_LONG).show()
            } else {
                val note = prependNote(getTimeStamp(), messageToSave)
                printAllNotes()
                saveDiary()
            }
        }
    }

    private fun createDialog(): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle("Remove last note")
            .setMessage("Do you really want to remove the last writing? This operation cannot be undone!")

            .setPositiveButton(android.R.string.yes) { _, _ ->
                removeNote()
                printAllNotes()
                saveDiary()
            }

            .setNegativeButton(android.R.string.no) { _, _ ->
            }
            .create()
    }

    private fun loadDiary() {
        val savedDiaryText = sharedPreferences.getString(KEY_DIARY_TEXT, null)

        if (!savedDiaryText.isNullOrBlank()) {
            noteList.clear()

            val allNotes = savedDiaryText.split("\n\n")

            for (aNote in allNotes) {
                val (date, messageToSave) = aNote.split("\n")
                appendNote(date, messageToSave)
            }

            printAllNotes()
        }
    }

    private fun printAllNotes() {
        clearField()
        tvDiary.text = noteList.joinToString("\n\n")
    }

    private fun saveDiary() {
        val editor = sharedPreferences.edit()
        val text = tvDiary.text.toString()
        editor.putString(KEY_DIARY_TEXT, text)
        editor.apply()
    }

    private fun removeNote() {
        if (noteList.size > 0) {
            noteList.removeAt(0)
        }
    }

    private fun getTimeStamp(): String {
        val instant = Clock.System.now()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return simpleDateFormat.format(instant.toEpochMilliseconds())
    }

    private fun prependNote(date: String, messageToSave: String) {
        noteList.add(0, Note(date, messageToSave))
    }

    private fun appendNote(date: String, messageToSave: String) {
        noteList.add(Note(date, messageToSave))
    }

    private fun clearField() {
        etNewWriting.text = null
    }

    private fun bindViews() {
        etNewWriting = findViewById(R.id.etNewWriting)
        btnSave = findViewById(R.id.btnSave)
        tvDiary = findViewById(R.id.tvDiary)
        alertDialogButton = findViewById(R.id.btnUndo)
    }
}