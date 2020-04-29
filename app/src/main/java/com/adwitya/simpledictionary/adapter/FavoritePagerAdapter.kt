package com.adwitya.simpledictionary.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.adwitya.simpledictionary.R
import com.adwitya.simpledictionary.ui.favorite.FavEngIdnFragment
import com.adwitya.simpledictionary.ui.favorite.FavIdnEngFragment

class FavoritePagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    @StringRes
    private val tabTitles = intArrayOf(
        R.string.idneng,
        R.string.engidn
    ) //array for contain tab titles

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) { //get fragment by position
            0 -> fragment =
                FavIdnEngFragment()
            1 -> fragment =
                FavEngIdnFragment()
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = tabTitles.size //get title count

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitles[position]) //get page/tab title from tabTitles array by position
    }
}