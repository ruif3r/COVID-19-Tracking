package com.example.ncov19traking

import android.content.Context
import androidx.appcompat.app.AlertDialog

class AlertDialogBuilder {

    fun aboutBuilder(context : Context){
        AlertDialog.Builder(context).setTitle("About").setMessage("COVID-19 Tracking by ruif3r\n" +
                "<ruif3rofficial@gmail.com>\n" +
                "github.com/ruif3r\n" +
                "Data Source:\n" +
                "github.com/NovelCOVID/API/\n" +
                "worldometers.info/\n" +
                "github.com/CSSEGISandData/COVID-19/").create().show()
    }
}