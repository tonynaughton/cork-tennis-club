package ie.wit.tennisapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.tennisapp.R
import ie.wit.tennisapp.activities.RegisterActivity
import ie.wit.tennisapp.adapters.MemberAdapter
import ie.wit.tennisapp.adapters.MembersListener
import ie.wit.tennisapp.databinding.FragmentMembersBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.MemberModel

class MembersFragment : Fragment(), MembersListener {

    lateinit var app: MainApp
    private var _fragBinding: FragmentMembersBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

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

        loadMembers()
        registerRefreshCallback()

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
        val launcherIntent = Intent(context, RegisterActivity::class.java)
        launcherIntent.putExtra("member_edit", member)
        refreshIntentLauncher.launch(launcherIntent)
    }

    override fun onDeleteMemberClick(member: MemberModel) {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setMessage("Are you sure you want to delete this member?")?.setCancelable(false)
            ?.setPositiveButton("Yes") { _, _ ->
                app.members.delete(member)
                fragBinding.recyclerView.adapter?.notifyDataSetChanged()
            }?.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        builder?.create()?.show()
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadMembers() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadMembers() {
        showMembers(app.members.findAll())
    }

    private fun showMembers(members: List<MemberModel>) {
        fragBinding.recyclerView.adapter = MemberAdapter(members, this)
        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}