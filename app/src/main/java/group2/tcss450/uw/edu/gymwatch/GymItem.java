package group2.tcss450.uw.edu.gymwatch;

/**
 * Created by james on 1/25/2017.
 */

public class GymItem {

    private String mGymName;
    private String mGymRating;
    private String mGymAddress;
    private String mGymFill;
    private int mGymImage;


    public GymItem(String theGymName, String theGymRating, String theGymAddress,
                   String theGymFill, int theGymImage) {
        mGymName = theGymName;
        mGymAddress = theGymAddress;
        mGymFill = theGymFill;
        mGymImage = theGymImage;
        mGymRating = theGymRating;

    }
    public String getmGymName() {
        return mGymName;
    }

    public void setmGymName(String mGymName) {
        this.mGymName = mGymName;
    }

    public String getmGymRating() {
        return mGymRating;
    }

    public void setmGymRating(String mGymRating) {
        this.mGymRating = mGymRating;
    }

    public String getmGymAddress() {
        return mGymAddress;
    }

    public void setmGymAddress(String mGymAddress) {
        this.mGymAddress = mGymAddress;
    }

    public String getmGymFill() {
        return mGymFill;
    }

    public void setmGymFill(String mGymFill) {
        this.mGymFill = mGymFill;
    }

    public int getmGymImage() {
        return mGymImage;
    }

    public void setmGymImage(int mGymImage) {
        this.mGymImage = mGymImage;
    }

}
