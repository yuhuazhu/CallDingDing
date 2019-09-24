package com.pdx.calldingding

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var requestCode = 0
    private lateinit var alarmManager: AlarmManager
    private val sp by lazy {
        getSharedPreferences("user", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestCode = sp.getInt("requestCode", 0)
        // 设置默认为今天
        val calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        tvDate.text = String.format("%s月%d号", mMonth + 1, mDay)
        tvTime.setOnClickListener { setTime() }
        tvDate.setOnClickListener { setDate() }
        tvAlarmAdd.setOnClickListener { setAlarm() }
        // 实例化AlarmManager
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            mYear = year
            mMonth = month
            mDay = dayOfMonth
            tvDate.text = String.format("%s月%s号", mMonth + 1, mDay)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        dialog.datePicker.minDate = System.currentTimeMillis()
        dialog.show()
    }

    private fun setTime() {
        val currentCalendar = Calendar.getInstance()
        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            mHour = hourOfDay
            mMinute = minute
            val format = if (minute > 9) "%s:%s" else "%s:0%s"
            tvTime.text = String.format(format, hourOfDay, minute)
            tvAlarmAdd.visibility = View.VISIBLE
            tvStatus.visibility = View.GONE
        }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true).show()
    }

    private fun setAlarm() {
        val currentCalendar = Calendar.getInstance()
        currentCalendar.set(Calendar.MONTH, mMonth)
        currentCalendar.set(Calendar.DAY_OF_MONTH, mDay)
        currentCalendar.set(Calendar.HOUR_OF_DAY, mHour)
        currentCalendar.set(Calendar.MINUTE, mMinute)
        currentCalendar.set(Calendar.SECOND, 0)
        // 设置闹钟触发动作
        val intent = Intent(this, AlarmBroadcast::class.java)
        intent.action = "startAlarm"
        requestCode++
        sp.edit().putInt("requestCode", requestCode).apply()
        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        // 设置时区（东八区）-需要加权限SET_TIME_ZONE
        // alarmManager.setTimeZone("GMT+08:00")
        // 设置闹钟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, currentCalendar.timeInMillis, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentCalendar.timeInMillis, pendingIntent)
        }
        tvAlarmAdd.visibility = View.GONE
        tvStatus.visibility = View.VISIBLE
    }
}
