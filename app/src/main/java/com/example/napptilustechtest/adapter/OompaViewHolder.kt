package com.example.napptilustechtest.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.napptilustechtest.databinding.OompaListItemBinding
import com.example.napptilustechtest.network.OompaDetailItem
import com.squareup.picasso.Picasso

class OompaViewHolder(view: View, private val listener: View.OnClickListener): RecyclerView.ViewHolder(view) {
    private val binding = OompaListItemBinding.bind(view)
    fun bind(oompa: OompaDetailItem) {
        binding.oompaCardName.text = "${oompa.firstName} ${oompa.lastName}"
        binding.oompaCardId.text = oompa.id.toString()
        binding.oompaCardProfession.text = oompa.profession
        Picasso.get().load(oompa.image).into(binding.oompaCardImage)
        itemView.setOnClickListener(listener)
        itemView.tag = oompa
    }
}