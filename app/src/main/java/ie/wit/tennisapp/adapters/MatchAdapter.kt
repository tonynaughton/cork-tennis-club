package ie.wit.tennisapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.tennisapp.databinding.CardResultBinding
import ie.wit.tennisapp.models.MatchModel

class MatchAdapter constructor(private var placemarks: List<MatchModel>) :
    RecyclerView.Adapter<MatchAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardResultBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = placemarks[holder.adapterPosition]
        holder.bind(placemark)
    }

    override fun getItemCount(): Int = placemarks.size

    class MainHolder(private val binding : CardResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(match: MatchModel) {
            binding.matchPlayerOne.text = match.playerOne
            binding.matchPlayerTwo.text = match.playerTwo
            binding.matchResult.text = match.result
        }
    }
}