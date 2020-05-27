package com.example.choreapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.choreapp.R
import com.example.choreapp.data.ChoresDatabaseHandler
import com.example.choreapp.model.Chore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var dbHandler: ChoresDatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = ChoresDatabaseHandler(this)
        if (!isDbEmpty()) callShowListActivity()

        btnSave.setOnClickListener {
            var chore : Chore = Chore()
            chore.ChoreName = txtChore.text.toString()
            chore.AssignedTo = txtAssignTo.text.toString()
            chore.AssignedBy = txtAssignBy.text.toString()

            saveChore(chore)

            txtChore.setText("")
            txtAssignTo.setText("")
            txtAssignBy.setText("")
            txtChore.requestFocus()

            callShowListActivity()
        }
    }

    fun isDbEmpty(): Boolean {
        return dbHandler!!.getChoresCountv2() == 0
    }

    fun saveChore(chore : Chore) : Chore {
        return dbHandler!!.createChore(chore)
    }

    fun callShowListActivity(){
        finish()
        startActivity(Intent(this, ChoreList::class.java))
    }
}
