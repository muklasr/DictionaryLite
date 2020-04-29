package com.adwitya.simpledictionary.ui.favorite


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.adwitya.simpledictionary.R
import com.adwitya.simpledictionary.adapter.ListAdapter
import com.adwitya.simpledictionary.database.FavIdEnHelper
import com.adwitya.simpledictionary.helper.MappingHelper
import com.adwitya.simpledictionary.model.Word
import com.adwitya.simpledictionary.ui.DetailActivity
import com.adwitya.simpledictionary.ui.DetailActivity.Companion.IDNENG
import com.adwitya.simpledictionary.ui.DetailActivity.Companion.TYPE_EXTRA
import com.adwitya.simpledictionary.ui.DetailActivity.Companion.WORD_EXTRA
import kotlinx.android.synthetic.main.fragment_fav_idn_eng.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavIdnEngFragment : Fragment() {

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    private lateinit var adapter: ListAdapter
    private lateinit var favIdnEngHelper: FavIdEnHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_fav_idn_eng, container, false)
        favIdnEngHelper = FavIdEnHelper(context as Context) //initialize helper
        favIdnEngHelper.open() //open the database

        //handle configuration change (screen orientation)
        if (savedInstanceState == null) {
            loadWordsAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Word>(EXTRA_STATE)
            if (list != null) {
                adapter.setData(list)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Word) {
                val i = Intent(context, DetailActivity::class.java)
                i.putExtra(WORD_EXTRA, data)
                i.putExtra(TYPE_EXTRA, IDNENG)
                startActivity(i)
            }
        })
        rvResult.layoutManager = LinearLayoutManager(context)
        rvResult.adapter = adapter

        loadWordsAsync()
    }

    private fun loadWordsAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredResult = async(Dispatchers.IO) {
                val cursor = favIdnEngHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val result = deferredResult.await()
            if (result.isNotEmpty()) {
                adapter.setData(result)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.list)
    }
}