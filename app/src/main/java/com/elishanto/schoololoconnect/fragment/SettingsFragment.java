package com.elishanto.schoololoconnect.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.elishanto.schoololoconnect.R;

import java.util.Arrays;
import java.util.List;

public class SettingsFragment extends PreferenceFragment {
    ListPreference useCard;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        useCard = (ListPreference) findPreference("USE_CARD");
        useCard.setValue(String.valueOf(sharedPreferences.getString("useCard", "0")));
        final List<String> arr = Arrays.asList(getResources().getStringArray(R.array.use_card));
        useCard.setSummary(arr.get(Integer.parseInt(sharedPreferences.getString("useCard", "0"))));
        useCard.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                sharedPreferences.edit().putString("useCard", (String) newValue).apply();
                return true;
            }
        });
    }
}
