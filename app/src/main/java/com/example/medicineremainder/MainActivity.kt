package com.example.medicineremainder
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var pillName: EditText
    private lateinit var pillDosage: EditText
    private lateinit var pillTime: TextView
    private lateinit var addPillButton: Button


    private var pillIdCounter = 0
    private var selectedHour = 0
    private var selectedMinute = 0
    companion object {
        val pillList = mutableListOf<Pill>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pillName = findViewById(R.id.pill_name)
        pillDosage = findViewById(R.id.pill_dosage)
        pillTime = findViewById(R.id.pill_time)
        addPillButton = findViewById(R.id.add_btn)

        pillTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            selectedHour = calendar.get(Calendar.HOUR_OF_DAY)
            selectedMinute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this,
                { _, hourOfDay, minute ->
                    selectedHour = hourOfDay
                    selectedMinute = minute
                    pillTime.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                }, selectedHour, selectedMinute, true)
            timePickerDialog.show()
        }

        addPillButton.setOnClickListener {
            addPill()
        }
    }

    private fun addPill() {
        val name = pillName.text.toString()
        val dosage = pillDosage.text.toString()
        val time = pillTime.text.toString()

        if (name.isNotEmpty() && dosage != null && time.isNotEmpty()) {
            val pill = Pill(pillIdCounter++, name, dosage, time)
            pillList.add(pill)
            Toast.makeText(this, "Pill Added: $name", Toast.LENGTH_SHORT).show()
            scheduleNotification(pill)
            pillName
            intent.putParcelableArrayListExtra("PILL_LIST", ArrayList( pillList))
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please enter all the details.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_icon -> {
                true
            }
            R.id.view_icon -> {
                val intent = Intent(this, ViewPill::class.java)
                intent.putParcelableArrayListExtra("PILL_LIST", ArrayList( pillList))
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(pill: Pill) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, selectedHour)
            set(Calendar.MINUTE, selectedMinute)
            set(Calendar.SECOND, 0)
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("PILL_NAME", pill.pillName)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            pill.pillId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}
