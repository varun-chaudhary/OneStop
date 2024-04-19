package com.example.onestop.taskmanager


object DataObject {
    var listdata = mutableListOf<CardInfo>()

    fun setData(title: String, priority: String) {
        listdata.add(CardInfo(title, priority,false))
    }

    fun getAllData(): List<CardInfo> {
        return listdata
    }

    fun getCompletedtask(): List<CardInfo>
    {
        val doneCards = listdata.filter { it.done }
        return doneCards
    }

    fun getIncompletetask(): List<CardInfo>
    {
        val doneCards = listdata.filter { !it.done }
        return doneCards
    }

    fun deleteAll(){
        listdata.clear()
    }

    fun getData(pos:Int): CardInfo {
        return listdata[pos]
    }

    fun deleteData(pos:Int){
        listdata.removeAt(pos)
    }

    fun updateData(pos:Int,title:String,priority:String,stt:Boolean)
    {
        listdata[pos].title=title
        listdata[pos].priority=priority
        listdata[pos].done=stt
    }

}