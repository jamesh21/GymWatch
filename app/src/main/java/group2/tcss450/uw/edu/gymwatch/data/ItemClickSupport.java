package group2.tcss450.uw.edu.gymwatch.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import group2.tcss450.uw.edu.gymwatch.R;

/**
 * Created by Dhruv on 3/1/2017.
 * This class is from http://www.littlerobots.nl/blog/Handle-Android-RecyclerView-Clicks/
 * It is open source and in public domain.
 */

public class ItemClickSupport {

    /** Reference to recycler view. */
    private final RecyclerView mRecyclerView;
    /** For the normal click. */
    private OnItemClickListener mOnItemClickListener;
    /** For the long click. */
    private OnItemLongClickListener mOnItemLongClickListener;

    /** Initialize and attaching the onclick listener for normal click. */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    /** Initialize and attaching the onclick listerner for long click .*/
    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLongClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                return mOnItemLongClickListener.onItemLongClicked(mRecyclerView,
                        holder.getAdapterPosition(), v);
            }
            return false;
        }
    };

    /** Initialize and attaching the listener to the recycle view. */
    private RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener);
            }
            if (mOnItemLongClickListener != null) {
                view.setOnLongClickListener(mOnLongClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
    };

    /**
     * Constructor for item click support.
     * @param recyclerView the recycle view
     */
    private ItemClickSupport(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(R.id.item_click_support, this);
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    /**
     * Method for adding click support.
     * @param view of the recycler view
     * @return a ItemClickSupport
     */
    public static ItemClickSupport addTo(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support == null) {
            support = new ItemClickSupport(view);
        }
        return support;
    }

    /**
     * Method for removing.
     * @param view of the recycler view
     * @return a ItemSupport
     */
    public static ItemClickSupport removeFrom(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support != null) {
            support.detach(view);
        }
        return support;
    }

    /**
     * Setting up the Item click listener.
      * @param listener which will be attached
     * @return the ItemClickSupport
     */
    public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    /**
     * Setting up the Item Click listener for long clicks.
     * @param listener which will be attached
     * @return the ItemClickSupport
     */
    public ItemClickSupport setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
        return this;
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(mAttachListener);
        view.setTag(R.id.item_click_support, null);
    }

    /**
     * Interface for the normal click.
     */
    public interface OnItemClickListener {

        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }

    /**
     * Interface for the long clicks.
     */
    public interface OnItemLongClickListener {

        boolean onItemLongClicked(RecyclerView recyclerView, int position, View v);
    }
}
