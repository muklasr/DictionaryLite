package com.muklas.dictionarylite.ui

import android.content.ContentValues
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.muklas.dictionarylite.R
import com.muklas.dictionarylite.database.DatabaseContract.IdToEnColumns.Companion.TRANSLATE
import com.muklas.dictionarylite.database.DatabaseContract.IdToEnColumns.Companion.WORD
import com.muklas.dictionarylite.database.DatabaseContract.IdToEnColumns.Companion.ID
import com.muklas.dictionarylite.database.FavEngIdnHelper
import com.muklas.dictionarylite.database.FavIdEnHelper
import com.muklas.dictionarylite.model.Word
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

class DetailActivity : AppCompatActivity() {
    companion object {
        const val WORD_EXTRA = "word_extra"
        const val TYPE_EXTRA = "type_extra"
        const val IDNENG = 0
        const val ENGIDN = 1
    }

    private lateinit var favEngIdnHelper: FavEngIdnHelper
    private lateinit var favIdnEngHelper: FavIdEnHelper
    private lateinit var dataWord: Word
    private lateinit var favoriteMenu: MenuItem
    private lateinit var textToSpeech: TextToSpeech
    private var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dataWord = intent.getParcelableExtra(WORD_EXTRA) as Word
        type = intent.getIntExtra(TYPE_EXTRA, 0)
        tvWord.text = dataWord.word
        tvTranslation.text = dataWord.translate

        favEngIdnHelper = FavEngIdnHelper(this)
        favIdnEngHelper = FavIdEnHelper(this)
        favEngIdnHelper.open()
        favIdnEngHelper.open()

        textToSpeech = TextToSpeech(this,
            TextToSpeech.OnInitListener { status ->
                if (status != TextToSpeech.ERROR)
                    textToSpeech.language = Locale.US
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        favoriteMenu = menu?.findItem(R.id.menu_favorite) as MenuItem
        setLove()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_favorite -> setFavorite()
            R.id.menu_speech -> textToSpeech.speak(
                dataWord.translate,
                TextToSpeech.QUEUE_FLUSH,
                null
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFavorite() {
        val values = ContentValues()
        values.put(ID, dataWord.id)
        values.put(WORD, dataWord.word)
        values.put(TRANSLATE, dataWord.translate)

        val cursor = if (type == IDNENG) {
            favIdnEngHelper.queryById(dataWord.id.toString())
        } else {
            favEngIdnHelper.queryById(dataWord.id.toString())
        }
        cursor.let {
            if (cursor.count > 0) {
                val result = if (type == IDNENG) {
                    favIdnEngHelper.deleteById(dataWord.id.toString())
                } else {
                    favEngIdnHelper.deleteById(dataWord.id.toString())
                }
                if (result > 0)
                    Toast.makeText(
                        this,
                        getString(R.string.success_delete),
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    Toast.makeText(this, getString(R.string.fail_delete), Toast.LENGTH_SHORT).show()
            } else {
                val result = if (type == IDNENG) {
                    favIdnEngHelper.insert(values)
                } else {
                    favEngIdnHelper.insert(values)
                }
                if (result > 0)
                    Toast.makeText(this, getString(R.string.success_add), Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, getString(R.string.fail_add), Toast.LENGTH_SHORT).show()
            }
        }
        setLove()
    }

    private fun setLove() {
        val cursor = if (type == IDNENG) {
            favIdnEngHelper.queryById(dataWord.id.toString())
        } else {
            favEngIdnHelper.queryById(dataWord.id.toString())
        }
        if (cursor.count > 0) {
            favoriteMenu.setIcon(R.drawable.ic_favorite_white_24dp)
        } else {
            favoriteMenu.setIcon(R.drawable.ic_favorite_border_white_24dp)
        }
    }
}
