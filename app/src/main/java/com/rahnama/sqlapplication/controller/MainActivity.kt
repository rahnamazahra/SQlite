package com.rahnama.sqlapplication.controller

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rahnama.sqlapplication.DataLayer.PhoneDatabaseHandler
import com.rahnama.sqlapplication.adapter.ContactAdapter
import com.rahnama.sqlapplication.databinding.ActivityMainBinding
import com.rahnama.sqlapplication.model.ContactModel
import com.rahnama.sqlapplication.model.DATABASE_NAME
import com.rahnama.sqlapplication.model.DATABASE_TABLE
import com.rahnama.sqlapplication.model.DATABASE_VERSION

private lateinit var binding:ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var dbhandler: PhoneDatabaseHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /*************************************/
        dbhandler = PhoneDatabaseHandler(this)
         checDB()
        /************************************************/
        binding.name.setText("")
        binding.family.setText("")
        binding.phone.setText("")
        /************************************************/
        binding.add.setOnClickListener {
            if (!TextUtils.isEmpty(binding.name.text.toString()) && !TextUtils.isEmpty(binding.family.text.toString()) && !TextUtils.isEmpty(
                    binding.phone.text.toString()))
                    {
                        val contactModel = ContactModel()
                        contactModel.name = binding.name.text.toString().trim()
                        contactModel.family = binding.family.text.toString().trim()
                        contactModel.phone = binding.phone.text.toString().trim()
                        saveData(contactModel)
                        Toast.makeText(this,"باموفقیت ذخیره شد",Toast.LENGTH_LONG).show()
                        binding.name.setText("")
                        binding.family.setText("")
                        binding.phone.setText("")
                        startActivity(Intent(this,ShowListActivity::class.java))
                        finish()

            }else{
                Toast.makeText(this,"تمامی فیلدها باید پر شوند",Toast.LENGTH_LONG).show()
            }
        }
    }

    /*****************************************************/
    fun checDB(){
         if(dbhandler!!.countContact()>0){
                startActivity(Intent(this,ShowListActivity::class.java))
            }

    }
    /*****************************************************/
    fun saveData(contactModel: ContactModel) {
        dbhandler!!.create(contactModel)
    }
    /************************************************/


}