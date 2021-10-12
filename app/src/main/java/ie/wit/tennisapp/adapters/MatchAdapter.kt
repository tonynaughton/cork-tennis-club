package ie.wit.tennisapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.tennisapp.databinding.CardResultBinding
import ie.wit.tennisapp.models.MatchModel

interface ResultsListener {
    fun onResultClick(result: MatchModel)
}

class MatchAdapter constructor(private var results: List<MatchModel>,
                               private val listener: ResultsListener) :
    RecyclerView.Adapter<MatchAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardResultBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val result = results[holder.adapterPosition]
        holder.bind(result, listener)
    }

    override fun getItemCount(): Int = results.size

    class MainHolder(private val binding : CardResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(match: MatchModel, listener: ResultsListener) {
            binding.matchPlayerOne.text = match.playerOne
            binding.matchPlayerTwo.text = match.playerTwo
            binding.matchResult.text = match.result
            binding.root.setOnClickListener { listener.onResultClick(match) }
        }
    }
}