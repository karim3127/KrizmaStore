package com.phonetact.phonetact.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Classe_Utils {

	public static void savePreferences(String key, String value,Context context) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String LoadPreferences(String key,Context context) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String value = sharedPreferences.getString(key, null);
		return value;
	}

    //sharend preference for integer

    public static void savePreferencesInt(String key, int value,Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int LoadPreferencesInt(String key,Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        int value = sharedPreferences.getInt(key, 0);
        return value;
    }

    // shared preference for list
    public static void saveListPreferences(String key, ArrayList<String> value,Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor scoreEditor = sharedPreferences.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(value);
        scoreEditor.putStringSet(key, set);
        scoreEditor.commit();
    }

    public static Set<String> LoadLisPreferences(String key,Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Set<String> value = new HashSet<String>();
        value = sharedPreferences.getStringSet(key, null);
        return value;
    }
}
