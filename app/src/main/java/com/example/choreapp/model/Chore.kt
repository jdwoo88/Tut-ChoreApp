package com.example.choreapp.model

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Chore() {
    private var dateFormat: java.text.DateFormat = DateFormat.getDateInstance()

    var ChoreName: String? = null
    var AssignedTo: String? = null
    var AssignedBy: String? = null
    var Id: Int? = null

    private var _timeAssigned: Long? = null
    var TimeAssigned: Long?
        get() = _timeAssigned
        set(value){
            _timeAssigned = value
            if (_timeAssigned != null) {
                var _date: Date = Date(_timeAssigned as Long)
                val _format = SimpleDateFormat("EEE, MMM dd, yyyy hh:mm a")
                FormattedDate = _format.format(_date)
            }
        }

    var FormattedDate: String? = null
        private set

    constructor(choreName: String, assignedTo: String, assignedBy: String, timeAssigned: Long, id: Int) : this(){
        ChoreName = choreName
        AssignedTo = assignedTo
        AssignedBy = assignedBy
        TimeAssigned = timeAssigned
        Id = id
    }
}