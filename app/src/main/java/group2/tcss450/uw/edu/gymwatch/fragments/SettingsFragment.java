package group2.tcss450.uw.edu.gymwatch.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import group2.tcss450.uw.edu.gymwatch.R;


public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener,
                                        View.OnClickListener,
                                                TimePickerFragment.DialogFragListener,
                                                CompoundButton.OnCheckedChangeListener {

    /** Reference to the settings view. */
    private View mView;

    private SharedPreferences mPrefs;
    @Override
    /**
     * This method initializes all the widgets within the settings fragment.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mPrefs = getActivity().getSharedPreferences(getString(R.string.SHARED_PREFS),
                                Context.MODE_PRIVATE);
        Bundle args = getArguments();
        int position = args.getInt(getString(R.string.POSITION));
        boolean isChecked = args.getBoolean(getString(R.string.NOTIFICATION));
        String startTime = args.getString(getString(R.string.START_TIME));
        String endTime = args.getString(getString(R.string.END_TIME));

        mView = inflater.inflate(R.layout.fragment_settings, container, false);

        //Setting the saved instance of the notification switch
        CheckBox notificationSwitch = (CheckBox) mView.findViewById(R.id.notification_switch);
        notificationSwitch.setOnCheckedChangeListener(this);
        notificationSwitch.setChecked(isChecked);

        //Setting the saved instance of the position for the spinner
        Spinner spinner = (Spinner) mView.findViewById(R.id.fill_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.fill_limit_percentages, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(position);

        // Initializing the buttons and adding listeners
        Button startButton = (Button) mView.findViewById(R.id.start_time_button);
        startButton.setOnClickListener(this);
        startButton.setText(startTime);
        Button finishButton = (Button) mView.findViewById(R.id.end_time_button);
        finishButton.setOnClickListener(this);
        finishButton.setText(endTime);

        return mView;
    }
    

    @Override
    public void onResume() {
        super.onResume();
        TextView title = (TextView) getActivity().findViewById(R.id.fragment_title);
        title.setText(R.string.action_settings);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mPrefs.edit().putInt(getString(R.string.POSITION), position).apply();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    /**
     * This onClick method will display a time picker dialog fragment.
     */
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putInt("button", v.getId());
        DialogFragment timeFrag = new TimePickerFragment();
        timeFrag.setArguments(args);
        timeFrag.setTargetFragment(this, 0);
        timeFrag.show(getFragmentManager(), "timePicker");
    }

    @Override
    /**
     * This method is used to set the time of the button passed.
     */
    public void toDialogFragToSettings(int buttonId, int hour, int minute) {
        switch(buttonId) {
            case R.id.start_time_button:
                Button startButton = (Button) mView.findViewById(R.id.start_time_button);
                String startText;
                if (minute < 10) {
                    startText = hour + " : 0" + minute;
                    startButton.setText(startText);
                } else {
                    startText = hour + " : " + minute;
                    startButton.setText(startText);
                }
                mPrefs.edit().putString(getString(R.string.START_TIME), startText).apply();
                break;
            case R.id.end_time_button:
                Button endButton = (Button) mView.findViewById(R.id.end_time_button);
                String endText;
                if (minute < 10) {
                    endText = hour + " : 0" + minute;
                    endButton.setText(endText);
                } else {
                    endText = hour + " : " + minute;
                    endButton.setText(endText);
                }
                mPrefs.edit().putString(getString(R.string.END_TIME), endText).apply();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mPrefs.edit().putBoolean(getString(R.string.NOTIFICATION), isChecked).apply();
    }
}
