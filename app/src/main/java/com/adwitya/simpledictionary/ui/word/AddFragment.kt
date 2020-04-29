package com.adwitya.simpledictionary.ui.word

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.adwitya.simpledictionary.R
import com.adwitya.simpledictionary.database.DatabaseContract.MyIdEnColumns.Companion.TRANSLATE
import com.adwitya.simpledictionary.database.DatabaseContract.MyIdEnColumns.Companion.WORD
import com.adwitya.simpledictionary.database.MyEngIdnHelper
import com.adwitya.simpledictionary.database.MyIdnEngHelper
import com.adwitya.simpledictionary.model.Word
import com.adwitya.simpledictionary.ui.DetailActivity.Companion.ENGIDN
import com.adwitya.simpledictionary.ui.DetailActivity.Companion.IDNENG
import kotlinx.android.synthetic.main.fragment_add.*

class AddFragment(val word: Word?, private var type: Int? = 0) : BottomSheetDialogFragment(),
    View.OnClickListener,
    RadioGroup.OnCheckedChangeListener {

    private lateinit var myIdnEngHelper: MyIdnEngHelper
    private lateinit var myEngIdnHelper: MyEngIdnHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myIdnEngHelper = MyIdnEngHelper(context as Context)
        myEngIdnHelper = MyEngIdnHelper(context as Context)
        myIdnEngHelper.open()
        myEngIdnHelper.open()

        btnSave.setOnClickListener(this)
        switchMode.setOnCheckedChangeListener(this)

        if (word != null) {
            setData()
            tvDialogTitle.text = getString(R.string.update_word)
            switchMode.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSave -> {
                if (word != null) updateWord()
                else addWord()
                MyIdnEngFragment.loadWordsAsync()
                MyEngIdnFragment.loadWordsAsync()
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.mode1 -> type = IDNENG
            R.id.mode2 -> type = ENGIDN
        }
    }

    private fun addWord() {
        val values = ContentValues()
        values.put(WORD, etWord.text.toString())
        values.put(TRANSLATE, etTranslation.text.toString())

        val result = if (type == IDNENG)
            myIdnEngHelper.insert(values)
        else
            myEngIdnHelper.insert(values)

        if (result > 0)
            Toast.makeText(context, getString(R.string.success_add), Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, getString(R.string.fail_add), Toast.LENGTH_SHORT).show()

        dialog.dismiss()
    }

    private fun setData() {
        word?.let {
            etWord.setText(word.word)
            etTranslation.setText(word.translate)
        }
    }

    private fun updateWord() {
        val values = ContentValues()
        values.put(WORD, etWord.text.toString())
        values.put(TRANSLATE, etTranslation.text.toString())

        val result = if (type == IDNENG)
            myIdnEngHelper.update(word?.id.toString(), values)
        else
            myEngIdnHelper.update(word?.id.toString(), values)


        if (result > 0)
            Toast.makeText(context, getString(R.string.success_add), Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, getString(R.string.fail_add), Toast.LENGTH_SHORT).show()

        dialog.dismiss()
    }

}
