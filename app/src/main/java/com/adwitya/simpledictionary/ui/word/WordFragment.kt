package com.adwitya.simpledictionary.ui.word

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adwitya.simpledictionary.R
import com.adwitya.simpledictionary.adapter.MyWordPagerAdapter
import kotlinx.android.synthetic.main.fragment_word.*

class WordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter =
            MyWordPagerAdapter(context as Context, childFragmentManager)
        viewPager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewPager)

        fab.setOnClickListener {
            val addFragment = AddFragment(null)
            addFragment.show(childFragmentManager, "TAG")
        }
    }
}