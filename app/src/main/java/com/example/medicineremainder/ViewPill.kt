package com.example.medicineremainder

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.medicineremainder.MainActivity.Companion

class ViewPill : AppCompatActivity() {

    private lateinit var pillListView: ListView
    private lateinit var pillList: List<Pill>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pill)
        pillListView = findViewById(R.id.pill_listview)
        pillList = intent.getParcelableArrayListExtra("PILL_LIST") ?: emptyList()
        val adapter = PillAdapter(this, pillList) { pill ->
            val intent = Intent(this, PillDetailActivity::class.java)
            intent.putExtra("pill_id",pill.pillId)
            intent.putExtra("pill_name", pill.pillName)
            intent.putExtra("pill_dosage",pill.pillDosage)
            intent.putExtra("pill_time",pill.pillTime)
            startActivity(intent)
        }
        pillListView.adapter = adapter
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_icon -> {

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.view_icon -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
