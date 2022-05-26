package com.example.bookslibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class ListActivity : AppCompatActivity() {
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Obtain the FirebaseAnalytics instance.
        analytics = Firebase.analytics

        var listView = findViewById<ListView>(R.id.listView)

        var item_1 =
            item("C", "C programming is considered as the base for other programming languages");
        var item_2 = item("C++", "C++ is an object-oriented programming language.");
        var item_3 = item("Java", "Java is a programming language and a platform.");
        var item_4 =
            item(".Net", ".NET is a framework which is used to develop software applications.");
        var item_5 = item(
            "Kotlin",
            "Kotlin is a open-source programming language, used to develop Android apps and much more."
        );
        var item_6 =
            item("Ruby", "Ruby is an open-source and fully object-oriented programming language.");
        var item_7 = item(
            "Rails",
            "Ruby on Rails is a server-side web application development framework written in Ruby language."
        );
        var item_8 = item(
            "Python",
            "Python is interpreted scripting  and object-oriented programming language."
        );
        var item_9 = item("Java Script", "JavaScript is an object-based scripting language.");
        val language =
            arrayListOf(item_1, item_2, item_3, item_4, item_5, item_6, item_7, item_8, item_9)

        val myListAdapter = MyListAdapter(this, language)
        listView.adapter = myListAdapter

    }

    data class item(var language: String, var description: String, var checkbox: Boolean = false)
}