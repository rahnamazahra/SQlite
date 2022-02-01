package com.rahnama.sqlapplication.controller

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rahnama.sqlapplication.DataLayer.PhoneDatabaseHandler
import com.rahnama.sqlapplication.R
import com.rahnama.sqlapplication.adapter.ContactAdapter
import com.rahnama.sqlapplication.databinding.ActivityShowListBinding
import com.rahnama.sqlapplication.model.ContactModel
import com.rahnama.sqlapplication.model.DATABASE_TABLE

private lateinit var binding: ActivityShowListBinding
class ShowListActivity : AppCompatActivity() {
    public final var dbhandler: PhoneDatabaseHandler? = null
    var adapter: ContactAdapter?=null
    var arrayList:ArrayList<ContactModel>?=null
    var layoutManager: RecyclerView.LayoutManager?=null
   var alertDialogBuilder:AlertDialog.Builder?=null
   var alertDialog:AlertDialog?=null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding =ActivityShowListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /*************************************/
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        /*********************************************/
        dbhandler = PhoneDatabaseHandler(this)
        arrayList=ArrayList()
        arrayList=dbhandler!!.show_all_list()

        adapter= ContactAdapter(arrayList!!,this,{contact,position->
            alertDialogBuilder=AlertDialog.Builder(this)
            alertDialogBuilder?.setTitle("حذف")
            alertDialogBuilder?.setMessage("آیا از حذف این آیتم مطمعن هستید؟")
            alertDialogBuilder?.setPositiveButton("بلی", DialogInterface.OnClickListener{ _, _ ->
                deleteItem(contact,position)
                if(dbhandler!!.countContact()==0){
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
                else{
                    alertDialog!!.dismiss()
                }
            })
            alertDialogBuilder?.setNegativeButton("خیر",DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()

            })
            alertDialog=alertDialogBuilder!!.create()
            alertDialog!!.show()

          },{contact,position ->
            val view=layoutInflater.inflate(R.layout.popup_update,null)
            val name=view.findViewById<EditText>(R.id.edit_name)
            val family=view.findViewById<EditText>(R.id.edit_family)
            val phone=view.findViewById<EditText>(R.id.edit_phone)
            val btn_edit=view.findViewById<Button>(R.id.edit_btn)

            name.setText(contact.name)
            family.setText(contact.family)
            phone.setText(contact.phone)

            btn_edit.setOnClickListener {
                if (!TextUtils.isEmpty(name.text.toString()) && !TextUtils.isEmpty(family.text.toString()) && !TextUtils.isEmpty(
                        phone.text.toString()))
                {

                    contact.name = name.text.toString().trim()
                    contact.family = family.text.toString().trim()
                    contact.phone = phone.text.toString().trim()

                    updateData(contact,position)

                    Toast.makeText(this,"باموفقیت ویرایش شد", Toast.LENGTH_LONG).show()

                    alertDialog!!.dismiss()

                }else{
                    Toast.makeText(this,"تمامی فیلدها باید پر شوند", Toast.LENGTH_LONG).show()
                }

            }

            alertDialogBuilder=AlertDialog.Builder(this).setView(view)
            alertDialog=alertDialogBuilder!!.create()
            alertDialog!!.show()
        })


    layoutManager=LinearLayoutManager(this)
    binding.listContact.adapter=adapter
    binding.listContact.layoutManager=layoutManager
    binding.listContact.setHasFixedSize(true)

    adapter!!.notifyDataSetChanged()
/***********************************************/
        binding.swipe.setOnRefreshListener {
            adapter!!.notifyDataSetChanged()

            Handler().postDelayed(Runnable {
                binding.swipe.isRefreshing = false
            }, 1000)
        }
    }
    /***********************************************/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.add_contact){
            popupcreateContact()
        }
        if(item.itemId==R.id.delete_list){
            alertDialogBuilder=AlertDialog.Builder(this)
            alertDialogBuilder?.setTitle("حذف")
            alertDialogBuilder?.setMessage("آیا از حذف همه آیتم ها مطمعن هستید؟")
            alertDialogBuilder?.setPositiveButton("بلی", DialogInterface.OnClickListener{ _, _ ->
                clearDatabase()

            })
            alertDialogBuilder?.setNegativeButton("خیر",DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()

            })
            alertDialog=alertDialogBuilder!!.create()
            alertDialog!!.show()
        }
        return  super.onOptionsItemSelected(item)
    }
/********************************************/

    private fun popupcreateContact() {
            val view=layoutInflater.inflate(R.layout.popup,null)
            val name=view.findViewById<EditText>(R.id.popup_name)
            val family=view.findViewById<EditText>(R.id.popup_family)
            val phone=view.findViewById<EditText>(R.id.popup_phone)
            val btn_add=view.findViewById<Button>(R.id.popup_btn_add)
           btn_add.setOnClickListener {
               if (!TextUtils.isEmpty(name.text.toString()) && !TextUtils.isEmpty(family.text.toString()) && !TextUtils.isEmpty(
                      phone.text.toString()))
               {
                   val contactModel = ContactModel()
                   contactModel.name = name.text.toString().trim()
                   contactModel.family = family.text.toString().trim()
                   contactModel.phone = phone.text.toString().trim()
                   saveData(contactModel)
                   Toast.makeText(this,"باموفقیت ذخیره شد", Toast.LENGTH_LONG).show()
                         name.setText("")
                        family.setText("")
                        phone.setText("")

                     alertDialog!!.dismiss()

               }else{
                   Toast.makeText(this,"تمامی فیلدها باید پر شوند", Toast.LENGTH_LONG).show()
               }

          }

           alertDialogBuilder=AlertDialog.Builder(this).setView(view)
           alertDialog=alertDialogBuilder!!.create()
            alertDialog!!.show()
    }
    /********************************************/

    private fun updateData(contactModel: ContactModel,position:Int) {
        dbhandler!!.update(contactModel)
        arrayList!![position] = contactModel
        adapter!!.notifyItemChanged(position)
    }

    /********************************************/

    fun saveData(contactModel: ContactModel) {
        dbhandler!!.create(contactModel)
        arrayList!!.add(contactModel)
        adapter!!.notifyDataSetChanged()
    }
    /********************************************/
    fun clearDatabase() {
             dbhandler!!.clearDatabase()
           if(dbhandler!!.countContact()==0){
               startActivity(Intent(this,MainActivity::class.java))
               finish()
        }

    }
/******************************************************/
private fun deleteItem(contact: ContactModel, position: Int) {
    dbhandler!!.delete(contact)
    arrayList!!.removeAt(position)
    adapter!!.notifyItemRemoved(position)

}

    /***********************************************/


}