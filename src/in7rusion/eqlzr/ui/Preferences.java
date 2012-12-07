package in7rusion.eqlzr.ui;

import in7rusion.eqlzr.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
 
public final class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
	
}
