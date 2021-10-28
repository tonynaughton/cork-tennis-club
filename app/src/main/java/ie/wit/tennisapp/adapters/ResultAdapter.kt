package ie.wit.tennisapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.tennisapp.databinding.CardResultBinding
import ie.wit.tennisapp.models.MemberModel
import ie.wit.tennisapp.models.ResultModel

interface ResultsListener {
    fun onResultClick(result: ResultModel)
}

class ResultAdapter constructor(private var results: List<ResultModel>,
                                private val listener: ResultsListener) :
    RecyclerView.Adapter<ResultAdapter.MainHolder>() {

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

        fun bind(result: ResultModel, listener: ResultsListener) {
            binding.resultPlayerOne.text = result.playerOne
            binding.resultPlayerTwo.text = result.playerTwo
            binding.resultScore.text = result.score
            binding.root.setOnClickListener { listener.onResultClick(result) }
        }
    }
}