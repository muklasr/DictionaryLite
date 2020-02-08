package com.muklas.dictionarylite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muklas.dictionarylite.R
import com.muklas.dictionarylite.model.Word
import kotlinx.android.synthetic.main.item_result.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ItemViewHolder>(){
    private val list = ArrayList<Word>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setData(data: ArrayList<Word>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_result, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(word: Word){
            with(itemView){
                tvWord.text = word.word
                tvTranslate.text = word.translate

                setOnClickListener {

                }
            }
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: Word)
    }

}