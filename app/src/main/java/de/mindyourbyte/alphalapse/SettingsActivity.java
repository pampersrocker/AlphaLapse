package de.mindyourbyte.alphalapse;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends BaseActivity {

    Switch autofocusSwitch;
    Switch turnOffScreenSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        autofocusSwitch = (Switch) findViewById(R.id.autofocus_switch);
        turnOffScreenSwitch = (Switch) findViewById(R.id.turn_off_screen_switch);

        Settings settings = Settings.getInstance();
        settings.load(getApplicationContext());
        autofocusSwitch.setChecked(settings.Autofocus);
        turnOffScreenSwitch.setChecked(settings.TurnOffScreen);

        autofocusSwitch.setOnCheckedChangeListener(this::onAutofocusChanged);
        turnOffScreenSwitch.setOnCheckedChangeListener(this::onTurnOffScreenChanged);
    }

    private void onTurnOffScreenChanged(CompoundButton compoundButton, boolean checked) {
        Settings settings = Settings.getInstance();
        settings.TurnOffScreen = checked;
        settings.save(getApplicationContext());
    }

    private void onAutofocusChanged(CompoundButton compoundButton, boolean checked) {
        Settings settings = Settings.getInstance();
        settings.Autofocus = checked;
        settings.save(getApplicationContext());
    }
}
