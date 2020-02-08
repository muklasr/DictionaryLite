package com.muklas.dictionarylite.ui.translate

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.muklas.dictionarylite.R
import com.muklas.dictionarylite.adapter.ListAdapter
import com.muklas.dictionarylite.database.EnToIdHelper
import com.muklas.dictionarylite.database.IdToEnHelper
import com.muklas.dictionarylite.helper.MappingHelper
import kotlinx.android.synthetic.main.fragment_translate.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TranslateFragment : Fragment(), View.OnClickListener {

    private lateinit var homeViewModel: TranslateViewModel
    private var idToEn = true //Indonesian To English
    private lateinit var idToEnHelper: IdToEnHelper
    private lateinit var enToIdHelper: EnToIdHelper
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(TranslateViewModel::class.java)
        idToEnHelper = IdToEnHelper(context as Context)
        idToEnHelper.open()
        return inflater.inflate(R.layout.fragment_translate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSwitch.setOnClickListener(this)
        tvFrom.setOnClickListener(this)
        tvTo.setOnClickListener(this)
        etWord.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty())
                    loadData(s.toString())
            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSwitch, R.id.tvFrom, R.id.tvTo -> switchLanguage() //change mode
        }
    }

    private fun switchLanguage() {
        if (idToEn) {
            idToEn = false
            tvFrom.text = getString(R.string.english)
            tvTo.text = getString(R.string.indonesian)
        } else {
            idToEn = true
            tvFrom.text = getString(R.string.indonesian)
            tvTo.text = getString(R.string.english)
        }
    }

    private fun loadData(word: String) {
        adapter = ListAdapter()
        adapter.notifyDataSetChanged()
        rvResult.layoutManager = LinearLayoutManager(context)
        rvResult.adapter = adapter

        GlobalScope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                val cursor = idToEnHelper.queryByWord(word)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val result = deferred.await()
            if (result.isNotEmpty()) {
                adapter.setData(result)
            }
        }

    }

}