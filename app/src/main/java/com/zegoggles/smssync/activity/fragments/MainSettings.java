package com.zegoggles.smssync.activity.fragments;

import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.TwoStatePreference;

import com.zegoggles.smssync.App;
import com.zegoggles.smssync.R;
import com.zegoggles.smssync.preferences.AuthPreferences;

import static com.zegoggles.smssync.preferences.Preferences.Keys.DONATE;
import static com.zegoggles.smssync.preferences.Preferences.Keys.ENABLE_AUTO_BACKUP;

public class MainSettings extends SMSBackupPreferenceFragment {
    private AuthPreferences authPreferences;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        super.onCreatePreferences(bundle, rootKey);
        authPreferences = new AuthPreferences(getContext());
        findPreference(AdvancedSettings.Server.class.getName()).setSummaryProvider(new Preference.SummaryProvider() {
            @Override
            public CharSequence provideSummary(Preference preference) {
                if (authPreferences.usePlain() && authPreferences.isLoginInformationSet()) {
                    return authPreferences.toString();
                } else {
                    return getString(R.string.custom_imap_not_configured);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        App.register(this);

    }
    @Override
    public void onStop() {
        super.onStop();
        App.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        hideRemovedFeatures();
    }

    private void hideRemovedFeatures() {
        disableAndHidePreference(ENABLE_AUTO_BACKUP.key);
        disableAndHidePreference(DONATE.key);
        disableAndHidePreference("backup_mms");
        disableAndHidePreference("backup_calllog");
        disableAndHidePreference("restore_calllog");
    }

    private void disableAndHidePreference(String key) {
        final Preference preference = findPreference(key);
        if (preference == null) return;

        if (preference instanceof TwoStatePreference) {
            ((TwoStatePreference) preference).setChecked(false);
        }
        preference.setVisible(false);
        preference.setEnabled(false);
    }
}
