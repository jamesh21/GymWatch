package group2.tcss450.uw.edu.gymwatch.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;


public class TimePickerFragment extends DialogFragment
                                    implements TimePickerDialog.OnTimeSetListener {

    /** Reference of the button which opened this fragment.*/
    private int mButtonPressed;
    /** Reference to the fragment that opened this time picker. */
    private DialogFragListener mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback = (DialogFragListener) getTargetFragment();
    }

    @Override
    /**
     * Method is for creating a new time picker dialog.
     **/
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mButtonPressed = bundle.getInt("button");
        }
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCallback.toDialogFragToSettings(mButtonPressed,hourOfDay, minute);
    }

    /**
     * This interface is a listener which communicates from the dialog fragment to
     * the settings fragment
     */
    public interface DialogFragListener {

        /**
         * This method is for interaction between the Dialog Fragment and the Settings.
         * @param buttonId that opened the dialog fragment
         * @param hour the hour to be set in the dialog fragment
         * @param minute the minute to be set in the dialog fragment
         */
        void toDialogFragToSettings(int buttonId, int hour, int minute);
    }
}
