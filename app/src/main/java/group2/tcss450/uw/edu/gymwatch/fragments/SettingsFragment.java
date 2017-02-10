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
                                        View.OnClickListener, TimePickerFragment.OnAddFriendListener {

    //private OnFragmentInteractionListener mListener;

    private View mView;
    @Override
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
        Button timeButton = (Button) mView.findViewById(R.id.start_time_button);
        timeButton.setOnClickListener(this);
        timeButton = (Button) mView.findViewById(R.id.end_time_button);
        timeButton.setOnClickListener(this);

        return mView;
    }


//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putInt("button", v.getId());
        DialogFragment timeFrag = new TimePickerFragment();
        timeFrag.setArguments(args);
        timeFrag.setTargetFragment(this, 0);
        //newFragment.show(getSupportFragmentManager(), "timePicker");
        timeFrag.show(getFragmentManager(), "timePicker");
        System.out.println("IN HERE");
    }

    @Override
    public void onAddFriendSubmit(int buttonId, int hour, int minute) {
        switch(buttonId) {
            case R.id.start_time_button:
                Button startButton = (Button) mView.findViewById(R.id.start_time_button);
                startButton.setText(hour + " : " + minute);
                break;
            case R.id.end_time_button:
                Button endButton = (Button) mView.findViewById(R.id.end_time_button);
                endButton.setText(hour + " : " + minute);
                break;
        }
    }

//    @Override
//    public void onFragmentInteraction(int buttonId, int hour, int minute) {
//        switch(buttonId) {
//            case R.id.start_time_button:
//                Button startButton = (Button) mView.findViewById(R.id.start_time_button);
//                startButton.setText(hour + " : " + minute);
//                break;
//            case R.id.end_time_button:
//                Button endButton = (Button) mView.findViewById(R.id.end_time_button);
//                endButton.setText(hour + " : " + minute);
//                break;
//        }
//    }


//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(int buttonId, int hour, int minute);
//    }
}
