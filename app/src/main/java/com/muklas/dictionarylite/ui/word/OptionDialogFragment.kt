package com.muklas.dictionarylite.ui.word

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.muklas.dictionarylite.R
import com.muklas.dictionarylite.database.MyEngIdnHelper
import com.muklas.dictionarylite.database.MyIdnEngHelper
import com.muklas.dictionarylite.model.Word
import com.muklas.dictionarylite.ui.DetailActivity.Companion.IDNENG
import kotlinx.android.synthetic.main.fragment_option_dialog.*

class OptionDialogFragment(private val word: Word, private val type: Int) :
    BottomSheetDialogFragment(),
    View.OnClickListener {

    private lateinit var myIdnEngHelper: MyIdnEngHelper
    private lateinit var myEngIdnHelper: MyEngIdnHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myIdnEngHelper = MyIdnEngHelper(context as Context)
        myEngIdnHelper = MyEngIdnHelper(context as Context)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_option_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnUpdate.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnUpdate -> update()
            R.id.btnDelete -> delete()
        }
    }

    private fun update() {
        val updateDialog = AddFragment(word, type)
        updateDialog.show(childFragmentManager, "TAG")
    }

    private fun delete() {
        val result = if (type == IDNENG)
            myIdnEngHelper.deleteById(word.id.toString())
        else
            myEngIdnHelper.deleteById(word.id.toString())

        if (result > 0)
            Toast.makeText(context, getString(R.string.success_delete), Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, getString(R.string.fail_delete), Toast.LENGTH_SHORT).show()

        MyIdnEngFragment.loadWordsAsync()
        MyEngIdnFragment.loadWordsAsync()
        dialog.dismiss()
    }
}
