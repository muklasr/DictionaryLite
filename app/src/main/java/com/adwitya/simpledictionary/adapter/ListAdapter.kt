package com.adwitya.simpledictionary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adwitya.simpledictionary.R
import com.adwitya.simpledictionary.model.Word
import kotlinx.android.synthetic.main.item_result.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {
    val list = ArrayList<Word>() //ArrayList for contain the words
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(data: ArrayList<Word>) {
        list.clear() //clear the list
        list.addAll(data) //add given data(ArrayList of Word) to list(local list)
        notifyDataSetChanged() //notify if the data set changed
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) { //handle item click from another class
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        //inflate the layout(layout for item list)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = list.size //get the list item count

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position]) //bind the word data from list(by position) into layout
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(word: Word) {
            with(itemView) {
                tvWord.text = word.word //set the tvWord text to word value of word object
                tvTranslate.text = word.translate //set the tvTranslate text to translate value of word object

                setOnClickListener {
                    onItemClickCallback?.onItemClicked(word) //handle if item clicked
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Word)
    }

}