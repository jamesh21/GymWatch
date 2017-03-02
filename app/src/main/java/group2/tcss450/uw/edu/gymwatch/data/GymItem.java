package group2.tcss450.uw.edu.gymwatch.data;

/**
 * Created by james on 1/25/2017.
 */


/**
 * This class represents a single gym and all the information regarding that gym.
 */
public class GymItem {

    /** Name of the gyme. */
    private String mGymName;
    /** Rating for the gym. */
    private String mGymRating;
    /** Address for the gym. */
    private String mGymAddress;
    /** Fill Rate for the gym. */
    private String mGymFill;
    /** URL for the image of the gym. */
    private String mGymImage;

    private String mGymID;

    /**
     * Constructor for a gym item.
     * @param theGymName name of the gym
     * @param theGymRating rating of the gym
     * @param theGymAddress address of the gym
     * @param theGymFill fill rate of the gym
     * @param theGymImage image of the gym
     */
    public GymItem(String theGymName, String theGymRating, String theGymAddress,
                   String theGymFill, String theGymImage, String theGymID) {
        mGymName = theGymName;
        mGymAddress = theGymAddress;
        mGymFill = theGymFill;
        mGymImage = theGymImage;
        mGymRating = theGymRating;
        mGymID = theGymID;

    }

    /**
     * Getting for Gym Name.
     * @return gym name
     */
    public void setGymName(String gymName) {
        mGymName = gymName;
    }


    /**
     * Getter for gym rating.
     * @return gym rating
     */
    public void setGymRating(String gymRating) {
        mGymRating = gymRating;
    }


    /**
     * Getter for gym address.
     * @return gym address
     */
    public void setGymAddress(String gymAddress) {
        mGymAddress = gymAddress;
    }


    /**
     * Getter for gym fill rate.
     * @return gym fill rate
     */
    public void setGymFill(String gymFill) {
        mGymFill = gymFill;
    }


    /**
     * Getting for gym image.
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

}
