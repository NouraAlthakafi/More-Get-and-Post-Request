package com.example.moregetandpostrequest

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var etName: EditText
    lateinit var buttonO: Button
    lateinit var buttonG: Button
    lateinit var rvNames: RecyclerView
    lateinit var nameArray: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etName = findViewById(R.id.etName)
        buttonO = findViewById(R.id.buttonO)
        buttonG = findViewById(R.id.buttonG)
        rvNames = findViewById(R.id.rvNames)
        nameArray = arrayListOf()

        val apiInterface = APINames().getName()?.create(APIInterface::class.java)
        buttonO.setOnClickListener {
            etName.text.toString()
            val call: Call<namesListItem> = apiInterface!!.addName(namesListItem(etName.text.toString(), 0))
            call?.enqueue(object: Callback<namesListItem?> {
                override fun onResponse(
                    call: Call<namesListItem?>,
                    response: Response<namesListItem?>
                ) {
                    for(i in 1..3){
                        etName.text.clear()
                        etName.clearFocus()
                    }
                    buttonG.setBackgroundColor(Color.RED)
                    Toast.makeText(this@MainActivity, "Name Added", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<namesListItem?>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "No Name Added", Toast.LENGTH_LONG).show()
                }

            })
        }
        buttonG.setOnClickListener {
            val call: Call<ArrayList<namesListItem?>> = apiInterface!!.showNames()
            call?.enqueue(object: Callback<ArrayList<namesListItem?>> {
                override fun onResponse(
                    call: Call<ArrayList<namesListItem?>>,
                    response: Response<ArrayList<namesListItem?>>
                ) {
                    updateArrayJSON(response.body()!!)
                }

                override fun onFailure(call: Call<ArrayList<namesListItem?>>, t: Throwable) {
                    Log.d("MainActivity", "${t.message}")
                }
            })
        }
    }

    fun updateArrayJSON(body: ArrayList<namesListItem?>) {
        rvNames.adapter = RVnames(body)
        rvNames.layoutManager = LinearLayoutManager(this)
        //rvNames.adapter?.notifyDataSetChanged()
    }
}
