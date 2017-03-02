package group2.tcss450.uw.edu.gymwatch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import group2.tcss450.uw.edu.gymwatch.data.GymAdapter;
import group2.tcss450.uw.edu.gymwatch.data.GymListData;
import group2.tcss450.uw.edu.gymwatch.R;


/**
 * This class is for the myGyms fragment which will hold gyms added by the user.
 */
public class MyGymsFragment extends Fragment {

    @Override
    /**
     * Method initializes the recycle view and adds it to this fragment.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_gyms, container, false);
        RecyclerView gymRecView = (RecyclerView) view.findViewById(R.id.gym_home_list);
        gymRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        GymAdapter gymAdapter = new GymAdapter(GymListData.getList(), getActivity());
        gymRecView.setAdapter(gymAdapter);
        return view;
    }

}
