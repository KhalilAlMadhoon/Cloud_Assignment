package com.example.bookslibrary

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

class MyListAdapter (
    private val context: Activity,
    private val dataSource: ArrayList<ListActivity.item>
) : BaseAdapter() {

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(p0: Int): Any {
        return dataSource[p0]
    }

    fun getListItem(position: Int): ListActivity.item? {
        return getItem(position) as ListActivity.item
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list1, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
        val subtitleText = rowView.findViewById(R.id.description) as TextView
        val cb_heart = rowView.findViewById(R.id.cb_heart) as CheckBox
        val btn_buy = rowView.findViewById(R.id.btn_buy) as Button

        // 1
        val item = getItem(position) as ListActivity.item

        // 2
        titleText.text = item.language
        subtitleText.text = item.description
        cb_heart.setTag(position)
        cb_heart.setChecked(item.checkbox)
        cb_heart.setOnCheckedChangeListener { checkBox, ischecked ->
            if (ischecked) {
                FirebaseAnalytics.getInstance(context).logEvent("add_wishlist") {
                    param("book_name", item.language)
                }
                Toast.makeText(context, "ischecked : Yes", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "ischecked : No", Toast.LENGTH_SHORT).show()
            }
            getListItem(checkBox.tag as Int)!!.checkbox = ischecked
        }

        btn_buy.setOnClickListener {
            FirebaseAnalytics.getInstance(context).logEvent("purchased") {
                param("book_name", item.language)
            }

        }

        return rowView
    }
}