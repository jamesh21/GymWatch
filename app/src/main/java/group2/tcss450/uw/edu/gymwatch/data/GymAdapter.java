package group2.tcss450.uw.edu.gymwatch.data;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import group2.tcss450.uw.edu.gymwatch.R;

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
    private Context mContext;
    /**
     * Constructor for a GymAdapter.
     * @param theListData which represents the data of gyms to be placed in the recycle view
     * @param theContext the context
     */
    public GymAdapter(List<GymItem> theListData, Context theContext) {
        mListData = theListData;
        mInflater = LayoutInflater.from(theContext);
        mContext = theContext;
    }

    @Override
    public GymHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.gym_list_item, parent, false);

        return new GymHolder(view);
    }



    @Override
    /**
     * Method used to set the gym attributes.
     */
    public void onBindViewHolder(GymHolder holder, int position) {
        GymItem item = mListData.get(position);
        item.setmGymPosition(position);
        holder.mGymName.setText(item.getGymName());

        Picasso.with(mContext)
                .load(item.getGymImage())
                .into(holder.mGymImage);
        if(!item.getGymRating().equals("No Rating")) {
            holder.mGymRating.setRating(Float.parseFloat(item.getGymRating()));
        } else {
            holder.mGymRating.setRating(0);
        }

        holder.mGymFillRate.setText(item.getGymFill() + "% Full");
        holder.mGymAddress.setText(item.getGymAddress());

    }

    @Override
    /**
     * Returns the list size of the items to be displayed.
     */
    public int getItemCount() {
        return mListData.size();
    }

    /**
     * Inner Class for holding the gym attributes.
     */
    class GymHolder extends RecyclerView.ViewHolder {

        /** The name of the gym. */
        private TextView mGymName;
        /** The rating of the gym. */
        private RatingBar mGymRating;
        /** The address of the gym. */
        private TextView mGymAddress;
        /** The fill Rate of the gym. */
        private TextView mGymFillRate;
        /** The image of the gym. */
        private ImageView mGymImage;
        /** The container for the gym. */
        private View container;

        /**
         * Constructor for a GymHolder object.
         * @param itemView for the items
         */
        public GymHolder(View itemView) {
            super(itemView);

            mGymName = (TextView)itemView.findViewById(R.id.gym_name);
            mGymAddress = (TextView)itemView.findViewById(R.id.gym_address);
            mGymFillRate = (TextView)itemView.findViewById(R.id.gym_fill);
            mGymRating = (RatingBar) itemView.findViewById(R.id.gym_rating);
            mGymImage = (ImageView)itemView.findViewById(R.id.gym_picture);
            container = itemView.findViewById(R.id.gym_content_container);
        }


    }
}
