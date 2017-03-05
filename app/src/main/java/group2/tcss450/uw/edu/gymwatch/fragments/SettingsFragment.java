package group2.tcss450.uw.edu.gymwatch.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import group2.tcss450.uw.edu.gymwatch.R;


public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener,
                                        View.OnClickListener,
                                                TimePickerFragment.DialogFragListener{

    /** Reference to the settings view. */
    private View mView;
//    private SharedPreferences mPrefs;

    @Override
    /**
     * This method initializes all the widgets within the settings fragment.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        Spinner spinner = (Spinner) mView.findViewById(R.id.fill_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.fill_limit_percentages, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //mPrefs = getPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
        Button timeButton = (Button) mView.findViewById(R.id.start_time_button);
        timeButton.setOnClickListener(this);
        timeButton = (Button) mView.findViewById(R.id.end_time_button);
        timeButton.setOnClickListener(this);

        return mView;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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
                if (minute < 10) {
                    startButton.setText(hour + " : 0" + minute);
                } else {
                    startButton.setText(hour + " : " + minute);
                }
                break;
            case R.id.end_time_button:
                Button endButton = (Button) mView.findViewById(R.id.end_time_button);
                if (minute < 10) {
                    endButton.setText(hour + " : 0" + minute);
                } else {
                    endButton.setText(hour + " : " + minute);
                }
                break;
        }
    }
}
