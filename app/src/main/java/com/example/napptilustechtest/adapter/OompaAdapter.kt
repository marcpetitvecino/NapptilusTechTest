package com.example.napptilustechtest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.napptilustechtest.R
import com.example.napptilustechtest.network.OompaDetailItem
import com.example.napptilustechtest.network.OompaListResponse

class OompaAdapter(private val list: List<OompaDetailItem>, private val listener: View.OnClickListener): RecyclerView.Adapter<OompaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OompaViewHolder =
        OompaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.oompa_list_item, parent, false), listener)

    override fun onBindViewHolder(holder: OompaViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}