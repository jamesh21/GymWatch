package group2.tcss450.uw.edu.gymwatch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import group2.tcss450.uw.edu.gymwatch.R;
import group2.tcss450.uw.edu.gymwatch.data.GymAdapter;
import group2.tcss450.uw.edu.gymwatch.data.GymItem;
import group2.tcss450.uw.edu.gymwatch.data.GymListData;


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
        Log.d("Creating Gyms Fragment", "!!!");
        String username = getArguments().getString("username");
        System.out.println("This is the username" + username);
        View view = inflater.inflate(R.layout.fragment_my_gyms, container, false);
        RecyclerView gymRecView = (RecyclerView) view.findViewById(R.id.gym_home_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        gymRecView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                (gymRecView.getContext(), layoutManager.getOrientation());
        gymRecView.addItemDecoration(dividerItemDecoration);
        List<GymItem> gymList = new ArrayList<>();
        gymList = GymListData.getList(username);
        GymAdapter gymAdapter = new GymAdapter(gymList, getActivity());
        System.out.println(gymList + "!!!!!!!!!!!!!!!");
        gymRecView.setAdapter(gymAdapter);
        return view;
    }

}
