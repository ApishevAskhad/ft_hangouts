package ru.shcool21.gloras.ft_hangouts.ui

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.shcool21.gloras.ft_hangouts.R
import ru.shcool21.gloras.ft_hangouts.system.App
import ru.shcool21.gloras.ft_hangouts.system.CustomPreferences
import ru.shcool21.gloras.ft_hangouts.system.SmsBroadcastReceiver
import ru.shcool21.gloras.ft_hangouts.ui.fragment.HomeFragment
import ru.shcool21.gloras.ft_hangouts.ui.navigation.Navigation
import ru.shcool21.gloras.ft_hangouts.ui.navigation.NavigationHandler
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.inject.Inject

internal class MainActivity : AppCompatActivity(), NavigationHandler {

    companion object {
        private const val SEND_SMS_CODE = 245
    }

    @Inject
    lateinit var navigation: Navigation

    @Inject
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    @Inject
    lateinit var pref: CustomPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).daggerComponent.inject(this)

        navigation.setNavigationHandler(this)

        setContentView(R.layout.act_main)

        registerReceiver(smsBroadcastReceiver, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                && (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS), SEND_SMS_CODE)
            } else {
                openFragment(HomeFragment())
            }
        } else {
            //TODO https://developer.android.com/training/permissions/requesting.html
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == SEND_SMS_CODE) {
            if (permissions[0] == Manifest.permission.SEND_SMS && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && permissions[1] == Manifest.permission.RECEIVE_SMS && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                openFragment(HomeFragment())
            } else {
                Toast.makeText(this, "App need grant permission!", Toast.LENGTH_LONG).show()
                onExit()
            }
        }
    }

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.containerFrameLayout, fragment)
            .addToBackStack(fragment.tag)
            .commit()
    }

    override fun onBack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            onExit()
        }
    }

    override fun onExit() {
        finish()
    }

    //todo back pressed logic
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        val lastTime = pref.getLastTime()
        if (lastTime > -1) {
            val seconds = SimpleDateFormat("ss")
            val milliSeconds = SimpleDateFormat("SSS")
            val date = Timestamp(System.currentTimeMillis()).time.minus(lastTime)
            Toast.makeText(this, "${seconds.format(date)} s ${milliSeconds.format(date)} ms", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        super.onPause()
        pref.saveCurrentTime(System.currentTimeMillis())
    }

    override fun onDestroy() {
        smsBroadcastReceiver.onCancel()
        unregisterReceiver(smsBroadcastReceiver)
        pref.dropTime()
        super.onDestroy()
    }

}
