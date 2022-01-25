package com.zerodeg.chatrecyclerview.recyclerview

import android.util.Log

class ChatModel(var profile:String?, var name : String? = null, var content : String? = null) {

    private val TAG = "ChatModel"

    init {
        Log.d(TAG, "init: create chat model")
    }

}