package com.nativebatterylevel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.Arguments
import com.nativebatterylevel.NativeBatteryLevelSpec
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.ReactApplicationContext

class NativeBatteryLevelModule(
    reactContext: ReactApplicationContext
) : NativeBatteryLevelSpec(reactContext) {

    private lateinit var batteryReceiver: BroadcastReceiver



    override fun getName() = NAME

    override fun getBatteryLevel(promise: Promise?) {
        val batteryManager = reactApplicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val level = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        try {
            if(level > 0){
                promise?.resolve(level)
            }
        }catch (e: Exception){
            promise?.reject("ERR_BATTERY_LEVEL", e.message)
        }


    }


    override fun getBatteryState(promise: Promise?) {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val status = reactApplicationContext.registerReceiver(null, filter)?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        try {
            when(status){
                BatteryManager.BATTERY_STATUS_CHARGING -> promise?.resolve("CHARGING")
                BatteryManager.BATTERY_STATUS_UNKNOWN -> promise?.resolve("UNKNOWN")
                BatteryManager.BATTERY_STATUS_FULL -> promise?.resolve("FULL")
                else -> promise?.resolve("UNPLUGGED")

            }
        }catch (e: Exception){
            promise?.reject("ERR_BATTERY_STATE", e.message)
        }

    }

    override fun isLowPowerModeEnabled(promise: Promise?) {
        val powerManager = reactApplicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isPowerSaveMode = powerManager.isPowerSaveMode
        try{
                promise?.resolve(isPowerSaveMode)

        }catch (e: Exception){
              promise?.reject("ERR_LOW_POWER_MODE", e.message)
        }


    }

    override fun isBatteryInfoAvailable(promise: Promise?) {
         promise?.resolve(true)
    }

    private fun isBatteryOptimizationIgnored(context: Context): Boolean {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isIgnoringBatteryOptimizations(context.packageName)

    }

    override fun isBatteryOptimizationEnabled(promise: Promise?) {

        try {
            val isIgnored = isBatteryOptimizationIgnored(reactApplicationContext)
            promise?.resolve(isIgnored)
        } catch (e: Exception) {
            promise?.reject("ERR_BATTERY_OPTIMIZATION", e.message)
        }
    }

    private fun stopListening(){
        reactApplicationContext.unregisterReceiver(batteryReceiver)
    }

    private fun sendEventData(level: Int,  status: Int?, mode: Boolean,){
        val stateValue =  when(status){
            BatteryManager.BATTERY_STATUS_CHARGING -> "CHARGING"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "UNPLUGGED"
            BatteryManager.BATTERY_STATUS_FULL -> "FULL"
            else -> "UNKNOWN"
        }
        val eventData = Arguments.createMap().apply {
            putInt("level", level)
            putString("state", stateValue)
            putBoolean("lowPowerMode", mode)
        }
        emitOnBatteryEvent(eventData)
    }

    override fun addBatteryListener(value: Boolean) {
        if(value){
            batteryReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (intent == null) return

                    val batteryManager = reactApplicationContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                    val level = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

                    val powerManager = reactApplicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
                    val isPowerSaveMode = powerManager.isPowerSaveMode

                    val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                    val status = reactApplicationContext.registerReceiver(null, filter)?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)


                    when (intent.action) {

                        // Battery level + charging state
                        Intent.ACTION_BATTERY_CHANGED -> {


                            sendEventData(level, status, isPowerSaveMode)


                        }

                        // Optionally observe plug/unplug for state updates
                        Intent.ACTION_POWER_CONNECTED -> {
                            sendEventData(level, status, isPowerSaveMode)
                        }

                        Intent.ACTION_POWER_DISCONNECTED -> {
                            sendEventData(level, status, isPowerSaveMode)
                        }

                        PowerManager.ACTION_POWER_SAVE_MODE_CHANGED -> {
                            sendEventData(level, status, isPowerSaveMode)
                        }


                    }
                }
            }

            // Register the receiver
            val filter = IntentFilter().apply {
                addAction(Intent.ACTION_BATTERY_CHANGED)
                addAction(Intent.ACTION_POWER_CONNECTED)
                addAction(Intent.ACTION_POWER_DISCONNECTED)
                addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
            }

            reactApplicationContext.registerReceiver(batteryReceiver, filter)
        }else {
            stopListening()
        }

    }

    override fun invalidate() {
        stopListening()
        super.invalidate()
    }



    companion object {
        const val NAME = "NativeBatteryLevel"
    }


}