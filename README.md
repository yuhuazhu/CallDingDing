# 嘿钉钉

### 功能介绍
可以定时打开钉钉（无需ROOT）
<br><br>
### 使用条件
1，需要取消锁屏密码，以便唤醒屏幕后能直接进入  
2，app设置好时间后，可以切换到后台但是进程不能被杀（重要）
<br><br>
### 打开其他应用
想要打开其他的应用，可以打开**AlarmBroadcast.kt**找到下面这行代码
`val dd = context.packageManager.getLaunchIntentForPackage("com.alibaba.android.rimet")`<br>
将括号里的`com.alibaba.android.rimet`改为你需要打开的**包名**即可
