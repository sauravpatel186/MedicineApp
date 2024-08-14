package com.example.medicineremainder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PillAdapter(
    context: Context,
    private val pills: List<Pill>,
    private val onItemClickListener: (Pill) -> Unit
) : ArrayAdapter<Pill>(context, 0, pills) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.pill_list_item, parent, false)

        val pill = pills[position]

        val pillName = view.findViewById<TextView>(R.id.pill_name)
        val pillDosage = view.findViewById<TextView>(R.id.pill_dosage)
        val pillTime = view.findViewById<TextView>(R.id.pill_time)

        pillName.text = pill.pillName
        pillDosage.text = pill.pillDosage.toString()
        pillTime.text = pill.pillTime


        view.setOnClickListener {
            onItemClickListener(pill)
        }

        return view
    }
}
