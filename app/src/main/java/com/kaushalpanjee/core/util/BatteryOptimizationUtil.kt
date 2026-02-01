package com.kaushalpanjee.core.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings

object BatteryOptimizationUtil {

    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
            return powerManager.isIgnoringBatteryOptimizations(context.packageName)
        }
        return true
    }

    fun openBatteryOptimizationSettings(context: Context) {
        val intent = Intent().apply {
            when {
                Build.MANUFACTURER.equals("xiaomi", ignoreCase = true) -> {
                    action = Intent.ACTION_MAIN
                    setClassName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                    )
                }
                Build.MANUFACTURER.equals("oppo", ignoreCase = true) -> {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", context.packageName, null)
                }
                Build.MANUFACTURER.equals("vivo", ignoreCase = true) -> {
                    action = Intent.ACTION_MAIN
                    setClassName(
                        "com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                    )
                }
                Build.MANUFACTURER.equals("huawei", ignoreCase = true) -> {
                    action = Intent.ACTION_MAIN
                    setClassName(
                        "com.huawei.systemmanager",
                        "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
                    )
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                    action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    data = Uri.parse("package:${context.packageName}")
                }
                else -> {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", context.packageName, null)
                }
            }
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback
            val fallbackIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(fallbackIntent)
        }
    }
}