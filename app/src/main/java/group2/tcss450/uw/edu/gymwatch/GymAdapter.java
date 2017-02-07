package group2.tcss450.uw.edu.gymwatch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by james on 1/25/2017.
 */

/**
 * This class is used to build all the gyms into the recycle view.
 */
public class GymAdapter extends RecyclerView.Adapter<GymAdapter.GymHolder> {

    /** The list of all the gyms to the view. */
    private List<GymItem> mListData;
    private LayoutInflater mInflater;

    public GymAdapter(List<GymItem> theListData, Context theContext) {
        mListData = theListData;
        mInflater = LayoutInflater.from(theContext);
    }
    @Override
    public GymHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gym_list_item, parent, false);
        return new GymHolder(view);
    }

    @Override
    public void onBindViewHolder(GymHolder holder, int position) {
        GymItem item = mListData.get(position);
        holder.mGymName.setText(item.getGymName());
        holder.mGymImage.setImageResource(item.getGymImage());
        holder.mGymRating.setText(item.getGymRating());
        holder.mGymFillRate.setText(item.getGymFill() + "% Full");
        holder.mGymAddress.setText(item.getGymAddress());
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    class GymHolder extends RecyclerView.ViewHolder {

        private TextView mGymName;
        private TextView mGymRating;
        private TextView mGymAddress;
        private TextView mGymFillRate;
        private ImageView mGymImage;
        private View container;

        public GymHolder(View itemView) {
            super(itemView);

            mGymName = (TextView)itemView.findViewById(R.id.gym_name);
            mGymAddress = (TextView)itemView.findViewById(R.id.gym_address);
            mGymFillRate = (TextView)itemView.findViewById(R.id.gym_fill);
            mGymRating = (TextView)itemView.findViewById(R.id.gym_rating);
            mGymImage = (ImageView)itemView.findViewById(R.id.gym_picture);
            container = itemView.findViewById(R.id.gym_content_container);
        }
    }
}
