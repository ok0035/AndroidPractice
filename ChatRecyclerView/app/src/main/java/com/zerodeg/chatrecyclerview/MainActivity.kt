package com.zerodeg.chatrecyclerview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.zerodeg.chatrecyclerview.databinding.ActivityMainBinding
import com.zerodeg.chatrecyclerview.firebase.ChatData
import com.zerodeg.chatrecyclerview.recyclerview.ChatAdapter
import com.zerodeg.chatrecyclerview.recyclerview.ChatInterface
import com.zerodeg.chatrecyclerview.recyclerview.ChatModel

class MainActivity : AppCompatActivity(), ChatInterface, View.OnClickListener, View.OnKeyListener {

    private val TAG = "MainActivity"

    private val modelList = ArrayList<ChatModel>()
    private lateinit var chatAdapter: ChatAdapter

    // Write a message to the database
    private val database = Firebase.database
    private val myRef = database.getReference("message")
    lateinit var binding: ActivityMainBinding

    init {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //test
//        for((i, item) in (1..10).withIndex()) {
//            var item :ChatModel = ChatModel("", "youngdo$i", "hello$i")
//            modelList.add(item)
//        }
        binding.tvSend.setOnClickListener(this)
        binding.etContent.setOnKeyListener(this)

        chatAdapter = ChatAdapter(this)
        chatAdapter.submitList(modelList)

        binding.recyclerView.apply {
            adapter = chatAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }


        // Write a message to the database
//        val database = Firebase.database
//        val myRef = database.getReference("message")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                Log.d(TAG, "Received message: $value")

                addChatItem(value)

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        //실시간 데이터 수신
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue<String>()
                Log.d(TAG, "onDataChange: ${post.toString()}")
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        database.reference.addValueEventListener(postListener)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addChatItem(content: String?) {
        val model: ChatModel = ChatModel("", "youngdo", content)
        this.modelList.add(model)
        binding.recyclerView.scrollToPosition(modelList.size - 1)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun clickChatItem(pos: Int) {
        Log.d(TAG, "clickChatItem: called")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvSend -> {
                var content = binding.etContent.text.toString()
                binding.etContent.setText("")
                writeMessage("Youngdo", content)
            }
        }
    }

    fun writeMessage(name: String, content: String) {
        val chatData = ChatData(name, content)
        database.reference.child("chat").setValue(chatData)
        myRef.setValue(content)
    }

    /*
    *
    * sendMessage 메소드에는 문제가 없다. (onClick 이벤트에서는 정상작동)
    * EditText에서 개행이 되면서 문제가 발생하는 듯 하다.
    * */

    override fun onKey(view: View?, keycode: Int, event: KeyEvent?): Boolean {

        when (keycode) {
            KeyEvent.KEYCODE_ENTER -> {
                if (binding.etContent.text.toString() == "" || binding.etContent.text.toString() == "\n") return false
                var content = binding.etContent.text.toString()
                binding.etContent.setText("")
                writeMessage("Youngdo", content)
            }
        }
        return false
    }


}