package com.abifarhan.githubuser.view.alarm

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.abifarhan.githubuser.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        private var alarmReceiver: AlarmReceiver = AlarmReceiver()

        private lateinit var switchPreference: SwitchPreference
        private lateinit var daily: String


        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            init()
            setSummaries()
            alarmReceiver = AlarmReceiver()
        }

        private fun init() {
            daily = resources.getString(R.string.alarm)
            switchPreference = findPreference<SwitchPreference>(daily) as SwitchPreference
        }

        private fun setSummaries() {
            val sh = preferenceManager.sharedPreferences
            switchPreference.isChecked = sh.getBoolean(daily, false)
        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            this.context
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            if (key == daily) {
                if (!switchPreference.isChecked) {
                    sharedPreferences!!.getBoolean(daily, false)
                    alarmReceiver.turnOfNotificationForDaily(
                        requireContext(),
                        AlarmReceiver.PESAN_HARIAN
                    )
                } else {
                    sharedPreferences!!.getBoolean(daily, true)
                    alarmReceiver.setNotificationForDaily(
                        requireContext(),
                        AlarmReceiver.PESAN_HARIAN
                    )
                }
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }
    }
}