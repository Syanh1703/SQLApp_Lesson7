package com.example.sqlapp

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_update_layout.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var itemList :ArrayList<DataModels>
    private lateinit var itemAdapters: ItemAdapters

    companion object
    {
        const val YES = "Yes, I'm sure"
        const val NO = "No"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Move the app name to center
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_24)
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setCustomView(R.layout.center_app_name)

        itemList = ArrayList()
        rvItemsList.layoutManager = LinearLayoutManager(this)
        itemAdapters = ItemAdapters(this, getItemList())
        rvItemsList.adapter = itemAdapters
        //Add Folder
        btnAddFolder.setOnClickListener {
            addFolder()
        }
    }

    private fun setUpFolderView()
    {
        if(getItemList().size>0)
        {
            itemList = ArrayList()
            rvItemsList.layoutManager = LinearLayoutManager(this)
            itemAdapters = ItemAdapters(this, getItemList())
            rvItemsList.adapter = itemAdapters
        }
    }
    private fun getItemList(): ArrayList<DataModels> {
        //Create instance for database handler
        val databaseHandler = DatabaseHandler(this)

        //Call the viewData to read the folders
        val folderList: ArrayList<DataModels> = databaseHandler.viewFolder()
        folderList.add(
            DataModels(
                0,
                "Tổng hợp tin tức thời sự",
                "Tổng hợp tin tức thời sự nóng hổi nhất, của tất cả các ..."
            )
        )
        folderList.add(DataModels(1, "Do It Your Self", "Sơn tùng MTP quá đẹp trai hát hay"))
        folderList.add(
            DataModels(
                2,
                "Cảm hứng sáng tạo",
                "Tổng hợp tin tức thời sự nóng hổi nhất, của tất cả các ..."
            )
        )

        folderList.add(
            DataModels(
                3,
                "Tổng hợp tin tức thời sự",
                "Tổng hợp tin tức thời sự nóng hổi nhất, của tất cả các ..."
            )
        )
        folderList.add(DataModels(4, "Do It Your Self", "Sơn tùng MTP quá đẹp trai hát hay"))
        folderList.add(
            DataModels(
                5,
                "Cảm hứng sáng tạo",
                "Tổng hợp tin tức thời sự nóng hổi nhất, của tất cả các ..."
            )
        )
        return folderList
    }

    private fun addFolder() {
        /**
         * Saving data into the database
         */
        val name = etFolderName.text.toString()
        val des = etFolderDes.text.toString()
        val databaseHandler = DatabaseHandler(this)
        if (name.isNotEmpty() && des.isNotEmpty()) {
            val status = databaseHandler.addData(DataModels(0, name, des))
            //Id is automatically increased
            if (status > -1) //-1 indicates error in adding data to the database
            {
                Toast.makeText(this, "Folder Added", Toast.LENGTH_SHORT).show()
                //Clear the Edit Text after
                etFolderName.text.clear()
                etFolderDes.text.clear()
                setUpFolderView()
            }
        } else {
            Toast.makeText(
                this,
                "Folder Name or Des cannot be saved",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun updateFolderDialog(data: DataModels)
    {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)//Set to false so it won't disappear if user click out the dialog
        updateDialog.setContentView(R.layout.dialog_update_layout)

        //Get the name
        updateDialog.etUpdateName.setText(data.folderName)
        updateDialog.etUpdateDes.setText(data.folderDes)

        updateDialog.tvUpdate.setOnClickListener{
            val name = updateDialog.etUpdateName.text.toString()
            val des = updateDialog.etUpdateDes.text.toString()

            val databaseHandler = DatabaseHandler(this)
            if(name.isNotEmpty() && des.isNotEmpty())
            {
                val status = databaseHandler.updateFolder(DataModels(data.id, name, des))
                if(status > -1)
                {
                    Toast.makeText(this, "Folder Updated", Toast.LENGTH_SHORT).show()
                    setUpFolderView()
                    updateDialog.dismiss()
                }
            }
            else
            {
                Toast.makeText(this, "Name and Description cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        updateDialog.tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        updateDialog.show()
    }

    fun deleteFolderDialog(data: DataModels)
    {
        val dialog = AlertDialog.Builder(this)
        //Set title for the Dialog
        dialog.setTitle("Delete Folder")
        dialog.setIcon(R.drawable.ic_baseline_warning_24)
        //dialog.setMessage("Are you sure to delete ${data.folderName}?")
        dialog.setMessage(Html.fromHtml("<font color='#F13210'>" + "Are you sure to delete ${data.folderName}?" + "</font>"))
        dialog.setPositiveButton(Html.fromHtml("<font color = '#F1EE10'$YES</font>"))
        {
            dialogInterface, which->
            //Create an instance for the Database Handler
            val databaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteFolder(DataModels(data.id, "", ""))
            if(status > -1)
            {
                Toast.makeText(this, "Folder deleted", Toast.LENGTH_SHORT).show()
                setUpFolderView()
            }
            dialogInterface.dismiss()
        }
        dialog.setNegativeButton("No")
        {
            dialogInterface, which ->
            run {
                dialogInterface.dismiss()
            }
        }

        //Create a Dialog
        val alertDialog = dialog.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}