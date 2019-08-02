package com.pdx.calldingding

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var mHour: Int = 0
    private var mMinute: Int = 0
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAlarm()
        tvAlarm.setOnClickListener {
            val currentCalendar = Calendar.getInstance()
            val dialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                mHour = hourOfDay
                mMinute = minute
                val format = if (minute > 9) {
                    "%s:%s"
                } else {
                    "%s:0%s"
                }
                tvAlarm.text = String.format(format, hourOfDay, minute)
                // 设置闹钟时间
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.set(Calendar.HOUR_OF_DAY, mHour)
                calendar.set(Calendar.MINUTE, mMinute)
                calendar.set(Calendar.SECOND, 0)
                setAlarm(calendar)
            }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true)
            dialog.show()
        }
    }

    // 初始化闹钟
    private fun initAlarm() {
        // 实例化AlarmManager
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // 设置闹钟触发动作
        val intent = Intent(this, AlarmBroadcast::class.java)
        intent.action = "startAlarm"
        pendingIntent = PendingIntent.getBroadcast(this, 110, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        // 设置时区（东八区）-需要加权限SET_TIME_ZONE
//        alarmManager.setTimeZone("GMT+08:00")
    }

    // 设置闹钟
    private fun setAlarm(calendar: Calendar) {
        //    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 5), pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        tvAlarm.text = SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(calendar.time) + " 唤醒"
    }
}
