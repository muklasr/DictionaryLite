package com.muklas.dictionarylite.ui.translate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.muklas.dictionarylite.R
import com.muklas.dictionarylite.adapter.ListAdapter
import com.muklas.dictionarylite.database.EngIdnHelper
import com.muklas.dictionarylite.database.IdnEngHelper
import com.muklas.dictionarylite.helper.MappingHelper
import com.muklas.dictionarylite.model.Word
import com.muklas.dictionarylite.ui.DetailActivity
import com.muklas.dictionarylite.ui.DetailActivity.Companion.ENGIDN
import com.muklas.dictionarylite.ui.DetailActivity.Companion.IDNENG
import com.muklas.dictionarylite.ui.DetailActivity.Companion.TYPE_EXTRA
import com.muklas.dictionarylite.ui.DetailActivity.Companion.WORD_EXTRA
import kotlinx.android.synthetic.main.fragment_translate.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class TranslateFragment : Fragment(), View.OnClickListener {

    private var idToEn = true //Indonesian To English
    private lateinit var idToEnHelper: IdnEngHelper
    private lateinit var enToIdHelper: EngIdnHelper
    private lateinit var adapter: ListAdapter
    private var type = 0
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        idToEnHelper = IdnEngHelper(context as Context)
        enToIdHelper = EngIdnHelper(context as Context)
        idToEnHelper.open()
        enToIdHelper.open()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textToSpeech = TextToSpeech(context,
            TextToSpeech.OnInitListener { status ->
                if (status != TextToSpeech.ERROR)
                    textToSpeech.language = Locale.US
            })
        btnSwitch.setOnClickListener(this)
        btnSpeech.setOnClickListener(this)
        tvFrom.setOnClickListener(this)
        tvTo.setOnClickListener(this)
        etWord.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                loadData(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSwitch, R.id.tvFrom, R.id.tvTo -> switchLanguage() //change mode
            R.id.btnSpeech -> textToSpeech.speak(
                etWord.text.toString(),
                TextToSpeech.QUEUE_FLUSH,
                null
            )
        }
    }

    private fun switchLanguage() {
        if (idToEn) {
            idToEn = false
            tvFrom.text = getString(R.string.english)
            tvTo.text = getString(R.string.indonesian)
            type = IDNENG
        } else {
            idToEn = true
            tvFrom.text = getString(R.string.indonesian)
            tvTo.text = getString(R.string.english)
            type = ENGIDN
        }
        loadData(etWord.text.toString())
    }

    private fun loadData(word: String) {
        var arg = word
        if (word.isEmpty())
            arg = "-"
        adapter = ListAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Word) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(WORD_EXTRA, data)
                intent.putExtra(TYPE_EXTRA, type)
                startActivity(intent)
            }
        })
        rvResult.layoutManager = LinearLayoutManager(context)
        rvResult.adapter = adapter

        GlobalScope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                val cursor = if (idToEn) {
                    idToEnHelper.queryByWord(arg)
                } else {
                    enToIdHelper.queryByWord(arg)
                }
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val result = deferred.await()
            if (result.isNotEmpty()) {
                adapter.setData(result)
            }
        }
    }

}