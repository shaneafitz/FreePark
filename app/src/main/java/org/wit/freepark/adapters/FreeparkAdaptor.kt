package org.wit.freepark.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.freepark.databinding.CardFreeparkBinding
import org.wit.freepark.models.FreeparkModel

interface FreeparkListener {
    fun onFreeparkClick(freepark: FreeparkModel)
}

class FreeparkAdapter constructor(private var freeparks: List<FreeparkModel>,
                                  private val listener: FreeparkListener) :
    RecyclerView.Adapter<FreeparkAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardFreeparkBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val freepark = freeparks[holder.adapterPosition]
        holder.bind(freepark, listener)
    }

    override fun getItemCount(): Int = freeparks.size

    class MainHolder(private val binding : CardFreeparkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(freepark: FreeparkModel, listener: FreeparkListener) {
            binding.freeparkLocation.text = freepark.location
            binding.description.text = freepark.description
            binding.root.setOnClickListener { listener.onFreeparkClick(freepark)}
        }
    }
}