package com.example.choreapp.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.choreapp.R
import com.example.choreapp.model.Chore
import kotlinx.android.synthetic.main.popup_chore.view.*


class ChoreListAdapter(private val list: ArrayList<Chore>,
                       private val context: Context) : RecyclerView.Adapter<ChoreListAdapter.ViewHolder>() {

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var dbHandler: ChoresDatabaseHandler? = null
        var choreName = itemView.findViewById(R.id.txtListChoreName) as TextView
        var assignTo = itemView.findViewById(R.id.txtListAssignedTo) as TextView
        var assignedDate = itemView.findViewById(R.id.txtListAssignedDate) as TextView
        var btnEdit = itemView.findViewById(R.id.btnListEdit) as Button
        var btnDelete = itemView.findViewById(R.id.btnListDelete) as Button

        fun bindView(chore: Chore){
            choreName.text = chore.ChoreName
            assignTo.text = chore.AssignedTo
            assignedDate.text = chore.FormattedDate

            var mPosition: Int? = null
            dbHandler = ChoresDatabaseHandler(context)

            btnEdit.setOnClickListener {
                mPosition = adapterPosition
                createPopupDialogForEdit(dbHandler!!, chore.Id!!.toInt(), mPosition!!, chore)
            }

            btnDelete.setOnClickListener {
                dbHandler!!.deleteChore(chore.Id!!.toLong())
                mPosition = adapterPosition
                list.removeAt(mPosition!!)
                notifyItemRemoved(mPosition!!)
            }
        }

        fun createPopupDialogForEdit(dbHandler: ChoresDatabaseHandler, choreId: Int, position: Int, existingChore: Chore){
            var dialogBuilder: AlertDialog.Builder? = null
            var dialog: AlertDialog? = null

            var view = LayoutInflater.from(context).inflate(R.layout.popup_chore, null)
            var choreName = view.txtChorePopUp
            var assignedTo = view.txtAssignToPopup
            var assignBy = view.txtAssignByPopup
            var saveButton = view.btnSavePopup

            choreName.setText(existingChore.ChoreName)
            assignedTo.setText(existingChore.AssignedTo)
            assignBy.setText(existingChore.AssignedBy)
            saveButton.setText("Update Chore")

            dialogBuilder = AlertDialog.Builder(context).setView(view)
            dialog = dialogBuilder!!.create()
            dialog!!.show()

            saveButton.setOnClickListener {
                existingChore.Id = existingChore.Id
                existingChore.ChoreName = choreName.text.toString()
                existingChore.AssignedTo = assignedTo.text.toString()
                existingChore.AssignedBy = assignBy.text.toString()

                dbHandler!!.updateChore(existingChore)
                notifyItemChanged(position, existingChore)
                dialog!!.dismiss()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}