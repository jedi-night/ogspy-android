package com.ogsteam.ogspy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.permission.CommonUtilities;
import com.ogsteam.ogspy.preferences.Accounts;
import com.ogsteam.ogspy.preferences.Preferences;
import com.ogsteam.ogspy.ui.DialogHandler;
import com.ogsteam.ogspy.utils.OgspyUtils;

import java.util.ArrayList;
import java.util.List;

public class OgspyPreferencesActivity extends PreferenceActivity {
    public static OgspyPreferencesActivity activity;

    private final static String LIBELLE_NOUVEAU_COMPTE = "Nouveau compte ...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.prefs);
        addPreferencesFromResource(R.xml.preferences);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        ListPreference accountsList = (ListPreference) findPreference("prefs_accounts");
        List<String> entries = new ArrayList<String>();
        List<String> entryValues = new ArrayList<String>();

        List<Account> accounts = OgspyActivity.activity.getHandlerAccount().getAllAccounts();
        for (Account acc : accounts) {
            entryValues.add(String.valueOf(acc.getId()));
            entries.add(acc.getUsername() + " - " + OgspyUtils.getUniversNameFromUrl(acc.getServerUnivers()));
        }
        // Ajout de la ligne de creation
        entryValues.add(String.valueOf(OgspyActivity.activity.getHandlerAccount().getAccountsCount()));
        entries.add(LIBELLE_NOUVEAU_COMPTE);

        accountsList.setEntries(entries.toArray(new CharSequence[entries.size()]));
        accountsList.setEntryValues(entryValues.toArray(new CharSequence[entryValues.size()]));
        accountsList.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        //CommonUtilities.displayMessage(OgspyActivity.activity, preference.getKey() + " new value=" + newValue);
                        if (Integer.parseInt((String) newValue) == OgspyActivity.activity.getHandlerAccount().getAccountsCount()) {
                            //CommonUtilities.displayMessage(OgspyActivity.activity, " Création d'un nouveau compte !");
                            Intent dialog = new Intent(activity, DialogActivity.class);
                            dialog.putExtra("type", DialogActivity.TYPE_NEW_ACCOUNT);
                            activity.startActivity(dialog);
                        } else {
                            Accounts.showAccount(OgspyActivity.activity, sharedPreferences);
                        }
                        return true;
                    }
                });

        activity = this;
    }

    @Override
    protected void onDestroy() {
        try {
            Accounts.saveAccount(OgspyActivity.activity, PreferenceManager.getDefaultSharedPreferences(this));
        } catch (OgspyException ogse){
            new DialogHandler().showException(OgspyActivity.activity, ogse);
        }
        try {
            Preferences.savePrefs(OgspyActivity.activity, PreferenceManager.getDefaultSharedPreferences(this));
        } catch (OgspyException ogse){
            new DialogHandler().showException(OgspyActivity.activity, ogse);
        }
        CommonUtilities.displayMessage(OgspyActivity.activity, "Préférences enregistrées");
        super.onDestroy();
    }

}
