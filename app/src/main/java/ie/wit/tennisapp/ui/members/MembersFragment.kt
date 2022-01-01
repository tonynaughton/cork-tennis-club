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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.tennisapp.R
import ie.wit.tennisapp.ui.auth.RegisterActivity
import ie.wit.tennisapp.adapters.MemberAdapter
import ie.wit.tennisapp.adapters.MembersListener
import ie.wit.tennisapp.databinding.FragmentMembersBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.MemberModel
import ie.wit.tennisapp.utils.SwipeToDeleteCallback
import ie.wit.tennisapp.utils.SwipeToEditCallback

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

        setEditAndDeleteSwipeFunctions()
        loadMembers()
        registerRefreshCallback()

        return root
    }

    private fun setEditAndDeleteSwipeFunctions() {
        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onMemberDeleteSwiped(viewHolder.adapterPosition)
            }
        }
        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onMemberEditSwiped(viewHolder.adapterPosition)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)
    }

    override fun onMemberDeleteSwiped(resultPosition: Int) {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setMessage("Are you sure you want to delete this member?")?.setCancelable(false)
            ?.setPositiveButton("Yes") { _, _ ->
                var targetResult = app.members.findAll().elementAt(resultPosition)
                val adapter = fragBinding.recyclerView.adapter as MemberAdapter
                app.members.delete(targetResult)
                adapter.notifyItemRemoved(resultPosition)
                fragBinding.recyclerView.adapter?.notifyDataSetChanged()
            }?.setNegativeButton("No") { dialog, _ ->
                fragBinding.recyclerView.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            }
        builder?.create()?.show()
    }

    override fun onMemberEditSwiped(memberPosition: Int) {
        val members = app.members.findAll()
        val targetMember = members[memberPosition]
        val launcherIntent = Intent(context, RegisterActivity::class.java)
        launcherIntent.putExtra("member_edit", targetMember)
        refreshIntentLauncher.launch(launcherIntent)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MembersFragment().apply {
                arguments = Bundle().apply { }
            }
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