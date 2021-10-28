package ie.wit.tennisapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.tennisapp.R
import ie.wit.tennisapp.adapters.MemberAdapter
import ie.wit.tennisapp.databinding.ActivityResultsListBinding
import ie.wit.tennisapp.main.MainApp
import ie.wit.tennisapp.adapters.ResultAdapter
import ie.wit.tennisapp.models.MemberModel
import ie.wit.tennisapp.models.ResultModel

class ListResultsActivity : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var binding: ActivityResultsListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = ResultAdapter(app.results.findAll())
        binding.toolbar.title = title

        binding.btnAdd.setOnClickListener() {
            val launcherIntent = Intent(this, AddResultActivity::class.java)
            startActivityForResult(launcherIntent, 0)
        }
        loadResults()
        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadResults() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_view_members -> {
                startActivity(Intent(this, ListMembersActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadResults() {
        showResults(app.results.findAll())
    }

    private fun showResults (results: List<ResultModel>) {
        binding.recyclerView.adapter = ResultAdapter(results)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}