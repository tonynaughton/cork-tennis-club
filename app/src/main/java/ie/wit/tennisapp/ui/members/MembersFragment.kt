package ie.wit.tennisapp.ui.members

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

    private lateinit var membersViewModel: MembersViewModel

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

        membersViewModel =
            ViewModelProvider(this).get(MembersViewModel::class.java)
        //val textView: TextView = root.findViewById(R.id.text_gallery)
        membersViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })

        val layoutManager = LinearLayoutManager(context)
        fragBinding.recyclerView.layoutManager = layoutManager
        fragBinding.recyclerView.adapter = MemberAdapter(app.members.findAll(), this)

        loadMembers()
        registerRefreshCallback()

        return root
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