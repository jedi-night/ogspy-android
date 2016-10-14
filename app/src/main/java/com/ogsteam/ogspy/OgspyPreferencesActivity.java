package com.ogsteam.ogspy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.ogsteam.ogspy.data.models.Account;
import com.ogsteam.ogspy.dialogs.DialogAccount;
import com.ogsteam.ogspy.permission.CommonUtilities;
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

        ListPreference accountsList = (ListPreference) findPreference("prefs_accounts");
        refreshAcountsList(accountsList);

        accountsList.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        Account lastAccount = OgspyActivity.getHandlerAccount().getLastAccount();
                        Intent dialog = new Intent(activity, DialogAccount.class);
                        // New account
                        if (lastAccount == null || (Integer.parseInt((String) newValue) > lastAccount.getId())) {
                            dialog.putExtra("type", DialogActivity.TYPE_ACCOUNT);
                            dialog.putExtra("creation", DialogActivity.ACCOUNT_NEW);
                            // Display account
                        } else {
                            dialog.putExtra("type", DialogActivity.TYPE_ACCOUNT);
                            dialog.putExtra("creation", DialogActivity.ACCOUNT_MODIFY);
                            dialog.putExtra("accountId", (String) newValue);
                        }
                        activity.startActivity(dialog);
                        return true;
                    }
                });

        activity = this;
    }

    @Override
    protected void onDestroy() {
        try {
            Preferences.savePrefs();
        }
        catch (OgspyException ogse){
            new DialogHandler().showException(this, ogse);
        }
        CommonUtilities.displayMessage(this, "Préférences enregistrées");
        super.onDestroy();
    }

    public void refreshAcountsList(ListPreference accountsList) {

        List<String> entries = new ArrayList<>();
        List<String> entryValues = new ArrayList<>();

        List<Account> accounts = OgspyActivity.getHandlerAccount().getAllAccounts();
        for (Account acc : accounts) {
            entryValues.add(String.valueOf(acc.getId()));
            entries.add(acc.getUsername() + " - " + OgspyUtils.getUniversNameFromUrl(acc.getServerUnivers()));
        }

        ListPreference accountSelectedList = (ListPreference) findPreference("prefs_account_selected");
        accountSelectedList.setEntries(entries.toArray(new CharSequence[entries.size()]));
        accountSelectedList.setEntryValues(entryValues.toArray(new CharSequence[entryValues.size()]));

        Account lastAccount = OgspyActivity.getHandlerAccount().getLastAccount();
        // Ajout de la ligne de creation
        entryValues.add(String.valueOf(lastAccount == null ? "0" : lastAccount.getId() + 1));
        entries.add(LIBELLE_NOUVEAU_COMPTE);

        accountsList.setEntries(entries.toArray(new CharSequence[entries.size()]));
        accountsList.setEntryValues(entryValues.toArray(new CharSequence[entryValues.size()]));

    }

}
