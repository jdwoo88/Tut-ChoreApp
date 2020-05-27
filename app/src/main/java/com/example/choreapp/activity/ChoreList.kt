package com.example.choreapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.choreapp.R
import com.example.choreapp.data.ChoreListAdapter
import com.example.choreapp.data.ChoresDatabaseHandler
import com.example.choreapp.model.Chore
import kotlinx.android.synthetic.main.activity_chore_list.*
import kotlinx.android.synthetic.main.popup_chore.view.*

class ChoreList : AppCompatActivity() {

    var dbHandler: ChoresDatabaseHandler? = null
    private var adapter : ChoreListAdapter? = null
    private var choreList: ArrayList<Chore>? = null
    private var choreListItems: ArrayList<Chore>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var dialogBuilder: AlertDialog.Builder? = null
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chore_list)

        // setup recycler view
        dbHandler = ChoresDatabaseHandler(this)
        choreList = ArrayList<Chore>()
        choreListItems = ArrayList<Chore>()
        layoutManager = LinearLayoutManager(this)
        adapter = ChoreListAdapter(choreListItems!!, this)

        recyclerViewChore.layoutManager = layoutManager // LinearLayoutManager(this)
        recyclerViewChore.adapter = adapter // ChoreListAdapter(choreList!!, this)

        // load list
        choreList = dbHandler!!.readAllChores()
        for (c in choreList!!.iterator()){
            //Log.d("SQLite", "${c.ChoreName}")
            var choreItem: Chore = Chore()
            choreItem.Id = c.Id
            choreItem.ChoreName = c.ChoreName
            choreItem.AssignedTo = c.AssignedTo
            choreItem.TimeAssigned = c.TimeAssigned

            choreListItems!!.add(choreItem)
        }

        adapter!!.notifyDataSetChanged()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_menu_button){
            // Toast.makeText(this, "Add Chore", Toast.LENGTH_SHORT).show()
            createPopupDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    fun createPopupDialog(){
        var view = layoutInflater.inflate(R.layout.popup_chore, null)
        var choreName = view.txtChorePopUp
        var assignedTo = view.txtAssignToPopup
        var assignBy = view.txtAssignByPopup
        var saveButton = view.btnSavePopup

        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogBuilder!!.create()
        dialog!!.show()

        saveButton.setOnClickListener {
            var chore : Chore = Chore()
            chore.ChoreName = choreName.text.toString()
            chore.AssignedTo = assignedTo.text.toString()
            chore.AssignedBy = assignBy.text.toString()

            choreListItems!!.add(0, saveChore(chore))
            adapter!!.notifyDataSetChanged()
            dialog!!.dismiss()
        }
    }

    fun saveChore(chore : Chore) : Chore {
        return dbHandler!!.createChore(chore)
    }
}
