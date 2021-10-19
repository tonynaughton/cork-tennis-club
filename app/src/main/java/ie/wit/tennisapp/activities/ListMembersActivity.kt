package ie.wit.tennisapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.tennisapp.R
import ie.wit.tennisapp.databinding.ActivityListMembersBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.adapters.MemberAdapter
import ie.wit.tennisapp.adapters.MembersListener
import ie.wit.tennisapp.models.MemberModel

class ListMembersActivity : AppCompatActivity(), MembersListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityListMembersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = MemberAdapter(app.members.findAll(), this)
        binding.toolbar.title = title

        binding.btnAdd.setOnClickListener() {
            val launcherIntent = Intent(this, AddMemberActivity::class.java)
            startActivityForResult(launcherIntent, 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMemberClick(member: MemberModel) {
        val launcherIntent = Intent(this, AddResultActivity::class.java)
        launcherIntent.putExtra("member_edit", member)
        startActivityForResult(launcherIntent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_view_results -> {
                startActivity(Intent(this, ListResultsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}