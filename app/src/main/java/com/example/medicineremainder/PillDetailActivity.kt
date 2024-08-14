package com.example.medicineremainder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.medicineremainder.MainActivity.Companion.pillList
import java.util.Calendar

class PillDetailActivity : AppCompatActivity() {

    private lateinit var pillNameTextView: EditText
    private lateinit var pillDosageTextView: EditText
    private lateinit var pillTimeTextView: TextView
    private lateinit var updateBtn: Button
    private var selectedHour = 0
    private var selectedMinute = 0

    private val CHANNEL_ID = "pill_update_channel"
    private val NOTIFICATION_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pill_detail)

        pillNameTextView = findViewById(R.id.pill_name)
        pillDosageTextView = findViewById(R.id.pill_dosage)
        pillTimeTextView = findViewById(R.id.pill_time)
        updateBtn = findViewById(R.id.update_btn)

        createNotificationChannel()

        val pillId = intent.extras?.getInt("pill_id")
        pillNameTextView.setText(intent.extras?.getString("pill_name") ?: "Pill Name")
        pillDosageTextView.setText(intent.extras?.getString("pill_dosage"))
        pillTimeTextView.text = intent.extras?.getString("pill_time").toString()

        val pill = MainActivity.pillList.find { it.pillId == pillId }

        pillTimeTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            selectedHour = calendar.get(Calendar.HOUR_OF_DAY)
            selectedMinute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this,
                { _, hourOfDay, minute ->
                    selectedHour = hourOfDay
                    selectedMinute = minute
                    pillTimeTextView.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                }, selectedHour, selectedMinute, true)
            timePickerDialog.show()
        }

        updateBtn.setOnClickListener {
            val updatedPillName = pillNameTextView.text.toString()
            val updatedPillDosage = pillDosageTextView.text.toString()
            val updatedPillTime = pillTimeTextView.text.toString()

            pillId?.let { id ->
                val updatedPill = Pill(id, updatedPillName, updatedPillDosage, updatedPillTime)
                updatePillDetails(updatedPill)
            }
        }
    }

    private fun updatePillDetails(updatedPillData: Pill) {
        val index = MainActivity.pillList.indexOfFirst { it.pillId == updatedPillData.pillId }
        if (index != -1) {
            MainActivity.pillList[index] = updatedPillData
            Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()
            sendNotification(updatedPillData)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pill Update Notifications"
            val descriptionText = "Notifications when pill data is updated"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(updatedPillData: Pill) {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_medication_24)
            .setContentTitle("Pill Updated")
            .setContentText("Your pill '${updatedPillData.pillName}' was successfully updated.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
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
                val intent = Intent(this, ViewPill::class.java)
                intent.putParcelableArrayListExtra("PILL_LIST", ArrayList( pillList))
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
