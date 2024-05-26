package com.example.storybook

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class StoryAdapter(private val dataSet: ArrayList<Story>):
        RecyclerView.Adapter<StoryAdapter.ViewHolder>(){

    private var onClickListener: OnClickListener ?= null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tv_title: TextView = view.findViewById(R.id.tvItemListTitle)
        val imageView = view.findViewById<ImageView>(R.id.itemListImage)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_title.setText(dataSet[position].title)
        holder.imageView.setImageResource(dataSet[position].image)

        holder.itemView.setOnClickListener {
            if (onClickListener!=null){
                onClickListener!!.onClick(position)
            }
        }

    }

    fun setOnClickListener(onCLickListener:OnClickListener){
        this.onClickListener = onCLickListener
    }

    interface OnClickListener{
        fun onClick(position: Int)
    }


}