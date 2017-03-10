package group2.tcss450.uw.edu.gymwatch.data;

/**
 * Created by james on 1/25/2017.
 */


import android.os.Parcel;
import android.os.Parcelable;


/**
 * This class represents a single gym and all the information regarding that gym.
 */
public class GymItem implements Parcelable{

    /** Name of the gym. */
    private String mGymName;
    /** Rating for the gym. */
    private String mGymRating;
    /** Address for the gym. */
    private String mGymAddress;
    /** Fill Rate for the gym. */
    private String mGymFill;
    /** URL for the image of the gym. */
    private String mGymImage;
    /**Position of the GymItem in the Recycler View**/
    private int mGymPosition;
    /** Gym ID. */
    private String mGymID;

    /**
     * Constructor for a gym item.
     * @param theGymName name of the gym
     * @param theGymRating rating of the gym
     * @param theGymAddress address of the gym
     * @param theGymFill fill rate of the gym
     * @param theGymImage image of the gym
     * @param theGymId id for the gym
     */
    public GymItem(String theGymName, String theGymRating, String theGymAddress,
                   String theGymFill, String theGymImage, String theGymId) {
        mGymName = theGymName;
        mGymAddress = theGymAddress;
        mGymFill = theGymFill;
        mGymImage = theGymImage;
        mGymRating = theGymRating;
        mGymID = theGymId;

    }

    /**
     * Setter for Gym Name.
     * @return gym name
     */
    public void setGymName(String gymName) {
        mGymName = gymName;
    }


    /**
     * Setter for gym rating.
     * @return gym rating
     */
    public void setGymRating(String gymRating) {
        mGymRating = gymRating;
    }


    /**
     * Setter for gym address.
     * @return gym address
     */
    public void setGymAddress(String gymAddress) {
        mGymAddress = gymAddress;
    }


    /**
     * Setter for gym fill rate.
     * @return gym fill rate
     */
    public void setGymFill(String gymFill) {
        mGymFill = gymFill;
    }


    /**
     * Setter for gym image.
     * @return gym image
     */
    public void setGymImage(String gymImage) {
        //Will add later
    }

    /**
     * Getting for Gym Name.
     * @return gym name
     */
    public String getGymName() {
        return mGymName;
    }


    /**
     * Getter for gym rating.
     * @return gym rating
     */
    public String getGymRating() {
        return mGymRating;
    }

    /**
     * Getting for gym id.
     * @return gym id
     */
    public String getGymID() {
        return mGymID;
    }

    /**
     * Getter for gym address.
     * @return gym address
     */
    public String getGymAddress() {
        return mGymAddress;
    }


    /**
     * Getter for gym fill rate.
     * @return gym fill rate
     */
    public String getGymFill() {
        return mGymFill;
    }


    /**
     * Getting for gym image.
     * @return gym image
     */
    public String getGymImage() {
        return mGymImage;
    }

    /**
     * Getting for gym position in list.
     * @return gym position
     */
    public int getmGymPosition() {return mGymPosition;};

    /**
     * Setting gym position.
     */
    public void setmGymPosition(int pos) {mGymPosition = pos;}

    /**Parcel Part**/
    public GymItem(Parcel in) {
        mGymName = in.readString();
        mGymAddress = in.readString();
        mGymRating = in.readString();
        mGymFill = in.readString();
        mGymImage = in.readString();
        mGymPosition = in.readInt();
        mGymID = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mGymName);
        dest.writeString(mGymAddress);
        dest.writeString(mGymRating);
        dest.writeString(mGymFill);
        dest.writeString(mGymImage);
        dest.writeInt(mGymPosition);
        dest.writeString(mGymID);
    }

    /**
     * For Creating a parcelable item.
     */
    public static final Parcelable.Creator<GymItem> CREATOR = new Parcelable.Creator<GymItem>() {

        @Override
        public GymItem createFromParcel(Parcel source) {
            return new GymItem(source);
        }

        @Override
        public GymItem[] newArray(int size) {
            return new GymItem[size];
        }
    };
}
