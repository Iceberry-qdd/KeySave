package com.iceberry.keysave.activity.nav.setting;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.iceberry.keysave.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }


}