package com.rahnama.sqlapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rahnama.sqlapplication.DataLayer.PhoneDatabaseHandler
import com.rahnama.sqlapplication.R
import com.rahnama.sqlapplication.model.ContactModel

class ContactAdapter(var listContact:ArrayList<ContactModel>, var context:Context, private val deleted:(ContactModel,Int)->Unit,
                     private val updated:(ContactModel,Int)->Unit):
    RecyclerView.Adapter<ContactAdapter.viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):viewHolder {
      val itemview=LayoutInflater.from(context).inflate(R.layout.list_row,parent,false)
        return viewHolder(itemview,deleted,updated)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

     holder.bindview(listContact[position],position)
    }

    override fun getItemCount(): Int {
     return  listContact.size
    }
    class viewHolder(itemview: View, val deleted: (ContactModel,Int) -> Unit, val updated: (ContactModel, Int) -> Unit) :RecyclerView.ViewHolder(itemview) {
        var name=itemview.findViewById<TextView>(R.id.Textname)
        var family=itemview.findViewById<TextView>(R.id.Textfamily)
        var phone=itemview.findViewById<TextView>(R.id.Textphone)
        var btn_delete=itemview.findViewById<ImageButton>(R.id.btn_delete)
        var btn_edit=itemview.findViewById<ImageButton>(R.id.btn_edit)


        fun bindview(contact:ContactModel,position: Int){
            name.text=contact.name.toString()
            family.text=contact.family.toString()
            phone.text=contact.phone.toString()

            btn_delete.setOnClickListener{
              deleted(contact,position)
            }
            btn_edit.setOnClickListener {
                updated(contact,position)
            }
        }

    }
}