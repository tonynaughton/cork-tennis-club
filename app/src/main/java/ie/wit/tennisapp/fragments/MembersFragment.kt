package ie.wit.tennisapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.tennisapp.R
import ie.wit.tennisapp.adapters.MemberAdapter
import ie.wit.tennisapp.adapters.MembersListener
import ie.wit.tennisapp.databinding.FragmentMembersBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.MemberModel

class MembersFragment : Fragment(), MembersListener {

    lateinit var app: MainApp
    private var _fragBinding: FragmentMembersBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentMembersBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.menu_members)

        val layoutManager = LinearLayoutManager(context)
        fragBinding.recyclerView.layoutManager = layoutManager
        fragBinding.recyclerView.adapter = MemberAdapter(app.members.findAll(), this)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MembersFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onEditMemberClick(member: MemberModel) {
        TODO("Not yet implemented")
    }

    override fun onDeleteMemberClick(member: MemberModel) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}