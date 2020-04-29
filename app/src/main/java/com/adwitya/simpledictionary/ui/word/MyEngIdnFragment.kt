package com.adwitya.simpledictionary.ui.word


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.adwitya.simpledictionary.R
import com.adwitya.simpledictionary.adapter.ListAdapter
import com.adwitya.simpledictionary.database.MyEngIdnHelper
import com.adwitya.simpledictionary.helper.MappingHelper
import com.adwitya.simpledictionary.model.Word
import com.adwitya.simpledictionary.ui.DetailActivity.Companion.ENGIDN
import kotlinx.android.synthetic.main.fragment_my_eng_idn.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class MyEngIdnFragment : Fragment() {

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"

        private lateinit var adapter: ListAdapter
        private lateinit var myEngIdnHelper: MyEngIdnHelper
        fun loadWordsAsync() {
            GlobalScope.launch(Dispatchers.Main) {
                val deferredResult = async(Dispatchers.IO) {
                    val cursor = myEngIdnHelper.queryAll()
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                val result = deferredResult.await()
                if (result.isNotEmpty()) {
                    adapter.setData(result)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_eng_idn, container, false)
        myEngIdnHelper = MyEngIdnHelper(context as Context) //initialize helper
        myEngIdnHelper.open() //open the database

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
                val optionDialogFragment = OptionDialogFragment(data, ENGIDN)
                optionDialogFragment.show(childFragmentManager, "TAG")
            }
        })
        rvResult.layoutManager = LinearLayoutManager(context)
        rvResult.adapter = adapter

        loadWordsAsync()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.list)
    }

    override fun onResume() {
        super.onResume()
        loadWordsAsync()
    }
}