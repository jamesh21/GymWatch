package group2.tcss450.uw.edu.gymwatch.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import group2.tcss450.uw.edu.gymwatch.R;


public class SearchResultsFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;
    private View mView;

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
//            TextView et = (TextView)getActivity().findViewById(R.id.text_testing);
//            et.setText(getArguments().getString("query"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search_results, container, false);
        TextView title = (TextView) getActivity().findViewById(R.id.fragment_title);
        SearchView search = (SearchView) getActivity().findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);
        search.setIconified(true);
        //TextView title = (TextView) mView.findViewById(R.id.fragment_title);
        title.setText("Results");
//        RecyclerView gymRecView = (RecyclerView) mView.findViewById(R.id.gym_rec_list);
//        gymRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        GymAdapter gymAdapter = new GymAdapter(GymListData.getList(), getActivity());
//        gymRecView.setAdapter(gymAdapter);
        return mView;
    }
}
