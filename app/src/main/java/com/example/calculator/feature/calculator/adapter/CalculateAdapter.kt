package com.example.calculator.feature.calculator.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.databinding.ItemCalculateBinding

class CalculateAdapter : RecyclerView.Adapter<CalculateAdapter.CalculateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CalculateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCalculateBinding.inflate(inflater, parent, false)
        return CalculateViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CalculateViewHolder,
        position: Int,
    ) {
    }

    override fun getItemCount(): Int = 3

    class CalculateViewHolder(
        binding: ItemCalculateBinding,
    ) : RecyclerView.ViewHolder(binding.root)
}
