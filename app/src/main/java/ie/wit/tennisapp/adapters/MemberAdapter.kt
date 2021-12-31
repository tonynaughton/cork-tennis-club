package ie.wit.tennisapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.tennisapp.databinding.CardMemberBinding
import ie.wit.tennisapp.models.MemberModel

interface MembersListener {
    fun onMemberEditSwiped(memberPosition: Int)
    fun onMemberDeleteSwiped(memberPosition: Int)
}

class MemberAdapter constructor(private var members: List<MemberModel>,
                                private val listener: MembersListener) :
    RecyclerView.Adapter<MemberAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMemberBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val member = members[holder.adapterPosition]
        holder.bind(member, listener)
    }

    override fun getItemCount(): Int = members.size

    class MainHolder(private val binding : CardMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(member: MemberModel, listener: MembersListener) {
            Picasso.get().load(member.image).resize(200,200).into(binding.imageIcon)
//            binding.editButton.setOnClickListener { listener.onEditMemberClick(member) }
//            binding.deleteButton.setOnClickListener { listener.onDeleteMemberClick(member) }

            binding.member = member
            binding.executePendingBindings()
        }
    }
}