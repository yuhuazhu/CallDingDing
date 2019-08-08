package com.pdx.calldingding

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.Toast

class AlarmBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if ("startAlarm" == intent.action) {
//            Toast.makeText(context, "闹钟提醒", Toast.LENGTH_LONG).show()
            // 处理闹钟事件
            // 振动、响铃、或者跳转页面等
//            val intent = Intent()
//            val componentName = ComponentName("com.alibaba.android.rimet", "com.beishen.nuzad.camera.JaundiceDetectActivity")
//            intent.component = componentName
//            intent.putExtra("token", token)
//            activity.startActivityForResult(intent, requestCode)
            Utils.wakeUpAndUnlock(context)
            Handler().postDelayed({
                val dd = context.packageManager.getLaunchIntentForPackage("com.alibaba.android.rimet")
                if (dd != null) {
                    dd.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(dd)
                }
            }, 666)
        }
    }
}
