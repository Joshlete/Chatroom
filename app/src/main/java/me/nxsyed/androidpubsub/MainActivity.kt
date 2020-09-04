package me.nxsyed.androidpubsub

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var userText = findViewById<EditText>(R.id.writeMessage)
        val sendButton = findViewById<Button>(R.id.sendButton)
        var chatList = findViewById<ListView>(R.id.messageList)

        // Create a List from String Array elements
        val arrayList = ArrayList<String>()

        // Create an ArrayAdapter from List
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList)

        // DataBind ListView with items from ArrayAdapter
        chatList.adapter = arrayAdapter

        // all messages and other events are received through receiver SubscribeCallback
        val receiver: SubscribeCallback = object : SubscribeCallback()  {
            override fun status(pubnub: PubNub, status: PNStatus) {} // must override this declared abstract method
            @SuppressLint("SetTextI18n")
            override fun message(pubnub: PubNub, message: PNMessageResult) {
                runOnUiThread {
                    if(arrayList.size >= 20) { // Stores set number of conversation strings
                        arrayList.removeAt(0)
                    }
                    if("\"${userText.text}\"" == message.message.toString()) {
                        arrayList.add("You >> ${message.message}")
                        arrayAdapter.notifyDataSetChanged();
                        userText.setText("")
                    } else {
                        arrayList.add("Not You >> ${message.message}")
                        arrayAdapter.notifyDataSetChanged();
                    }

                }
            }
            override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {} // must override this declared abstract method
        }

        /* accessing pubNub servers. To use your own account, create new account and replace  *
         * subscribeKey and publishKey with your own.                                         */
        val pnConfiguration = PNConfiguration()
        pnConfiguration.subscribeKey = "sub-c-cabe51a0-8f59-11ea-927a-2efbc014b69f"
        pnConfiguration.publishKey = "pub-c-d27741a7-bc8d-4d0c-bef6-c59bdd155b24"
        pnConfiguration.secretKey = "true"
        val pubNub = PubNub(pnConfiguration)

        // all published messages will come through this listener
        pubNub.run {
            addListener(receiver)
            subscribe().channels(listOf("anychannel")).execute()
        }


        // send message using send button
        sendButton.setOnClickListener {
            pubNub.run {
                if(userText.text.toString() != ""){ // don't send anything if user has no input
                    publish().message(userText.text.toString()).channel("anychannel").async(object : PNCallback<PNPublishResult>() {
                        override fun onResponse(result: PNPublishResult, status: PNStatus) {} })
                }
            }
        }

        // send message using enter button on virtual keyboard
        userText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if(userText.text.toString() != ""){ // don't send anything if user has no input
                    pubNub.run {
                        publish().message(userText.text.toString()).channel("anychannel").async(object : PNCallback<PNPublishResult>() {
                            override fun onResponse(result: PNPublishResult, status: PNStatus) {} })
                    }
                    return@OnKeyListener true
                }
            }
            false
        })


    }

}
