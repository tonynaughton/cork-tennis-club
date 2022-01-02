package ie.wit.tennisapp.ui.members

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import ie.wit.tennisapp.R
import ie.wit.tennisapp.ui.auth.RegisterActivity
import ie.wit.tennisapp.adapters.MemberAdapter
import ie.wit.tennisapp.adapters.MembersListener
import ie.wit.tennisapp.databinding.FragmentMembersBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.models.MemberModel
import ie.wit.tennisapp.helpers.SwipeToDeleteCallback
import ie.wit.tennisapp.helpers.SwipeToEditCallback

class MembersFragment : Fragment(), MembersListener {

    lateinit var app: MainApp
    private var _fragBinding: FragmentMembersBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)

        auth = FirebaseAuth.getInstance()
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
        fragBinding.recyclerView.adapter = MemberAdapter(app.members.findAll())

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

    override fun onMemberDeleteSwiped(memberPosition: Int) {
        val members = app.members.findAll()
        val targetMember = members[memberPosition]
        if (auth.currentUser!!.uid != targetMember.uuid) {
            Toast.makeText(context,
                getString(R.string.member_delete_denied),
                Toast.LENGTH_LONG).show()
            fragBinding.recyclerView.adapter?.notifyDataSetChanged()
        } else {
            val builder = context?.let { AlertDialog.Builder(it) }
            builder?.setMessage(getString(R.string.confirm_member_delete))
                ?.setCancelable(false)
                ?.setPositiveButton("Yes") { _, _ ->
                    val adapter = fragBinding.recyclerView.adapter as MemberAdapter
                    app.members.delete(targetMember)
                    adapter.notifyItemRemoved(memberPosition)
                    fragBinding.recyclerView.adapter?.notifyDataSetChanged()
                }?.setNegativeButton("No") { dialog, _ ->
                    fragBinding.recyclerView.adapter?.notifyDataSetChanged()
                    dialog.dismiss()
                }
            builder?.create()?.show()
        }
    }

    override fun onMemberEditSwiped(memberPosition: Int) {
        val members = app.members.findAll()
        val targetMember = members[memberPosition]
        if (auth.currentUser!!.uid != targetMember.uuid) {
            Toast.makeText(context,
                getString(R.string.member_edit_denied),
                Toast.LENGTH_LONG).show()
            fragBinding.recyclerView.adapter?.notifyDataSetChanged()
        } else {
            val launcherIntent = Intent(context, RegisterActivity::class.java)
            launcherIntent.putExtra("member_edit", targetMember)
            refreshIntentLauncher.launch(launcherIntent)
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
        fragBinding.recyclerView.adapter = MemberAdapter(members)
        fragBinding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}