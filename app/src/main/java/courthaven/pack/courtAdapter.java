package courthaven.pack;

//import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

public class courtAdapter extends ArrayAdapter<Court> {
    int[] imageResources = {R.drawable.tennis_court1,R.drawable.basketball_court2,R.drawable.football_court3,R.drawable.basketball_court4,
            R.drawable.tennis_court5,R.drawable.basketball_court6,R.drawable.tennis_court7,R.drawable.basketball_court8,
            R.drawable.football_court9,R.drawable.basketball_court10,R.drawable.tennis_court11,R.drawable.basketball_court12};

    String selectedDate = new String();
    public String getSelectedDate() { return selectedDate; }
    public void setSelectedDate(String selectedDate) { this.selectedDate = selectedDate; }

    private Context mContext;
    private ArrayList<Court> courtsList = new ArrayList<>();
    private ArrayList<Integer> favoritedCourtIds = new ArrayList<>();

    public void initializeFavoritedCourtIds(int userId) {
        loadFavoritedCourtIds(userId);
    }

    public courtAdapter(@NonNull Context context, @NonNull ArrayList<Court> list) {
        super(context, 0, list);
        mContext = context;
        courtsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.home_list_item, parent, false);
        }

        final View finalListItem = listItem; // Declare as final

        Court currentCourt = courtsList.get(position);

        currentCourt.setImageDrawable(imageResources[currentCourt.getId()-1]);

        ImageView image = listItem.findViewById(R.id.imageView_court);
        image.setBackgroundResource(currentCourt.getImageDrawable());

        TextView name = (TextView) listItem.findViewById(R.id.court_name);
        name.setText(currentCourt.getName());

        TextView address = (TextView) listItem.findViewById(R.id.court_address);
        address.setText(currentCourt.getAddress());

        TextView sport = (TextView) listItem.findViewById(R.id.court_sport);
        sport.setText(currentCourt.getSport());

        ImageView starIcon = listItem.findViewById(R.id.star_icon);
        ImageView bookIcon = listItem.findViewById(R.id.book_icon);

        // Check if the court is in the list of favorited court IDs
        boolean isCourtFavorited = favoritedCourtIds.contains(currentCourt.getId());

        // Set the star icon color based on whether the court is in favorites
        if (isCourtFavorited) {
            starIcon.setImageResource(R.drawable.star);
            starIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.yellow));
        } else {
            starIcon.setImageResource(R.drawable.favorites);
            starIcon.setColorFilter(null);
        }

        // Set OnClickListener for the star_icon
        starIcon.setOnClickListener(v -> {
            handleStarIconClick(currentCourt, finalListItem);
        });

        // Set OnClickListener for the book_icon
        bookIcon.setOnClickListener(v -> {
            selectedDate = homeFragment.getSelectedDate();
            openBookCourtFragment(currentCourt, v, selectedDate, SignInFragment.UserId);
        });


        return listItem;
    }

    private void openBookCourtFragment(Court court, View v, String date, int userId) {
        Fragment fragment = new bookCourtFragment();
        ((bookCourtFragment) fragment).setCourt(court);
        ((bookCourtFragment) fragment).setSelectedDate(String.valueOf(date));
        ((bookCourtFragment) fragment).setUserId(userId);
        ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_from_bottom, androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .replace(R.id.frameLayout, fragment)
                .commit();
    }


    private void loadFavoritedCourtIds(int userId) {
        // Retrieve the favorited courts' IDs from the database for the logged-in user
        String favoritedCourtsString = getUserFavoritesFromDatabase(userId);

        // Convert the comma-separated string into a list of integers
        favoritedCourtIds = convertStringToIntegerList(favoritedCourtsString);
    }

    private ArrayList<Integer> convertStringToIntegerList(String commaSeparatedString) {
        ArrayList<Integer> integerList = new ArrayList<>();
        String[] parts = commaSeparatedString.split(",");
        for (String part : parts) {
            try {
                int id = Integer.parseInt(part.trim());
                integerList.add(id);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Handle the case where the string is not a valid integer
            }
        }
        return integerList;
    }

    private void handleStarIconClick(Court court, View listItem) {
        int userId = SignInFragment.UserId;
        int courtId = court.getId();

        // Get the existing favorites for the logged-in user
        String currentUserFavorites = getUserFavoritesFromDatabase(userId);

        // Check if the court is currently favorited
        boolean isCourtCurrentlyFavorited = currentUserFavorites.contains(String.valueOf(courtId));

        // Toggle the isFavorite property
        boolean isCourtFavorite = !isCourtCurrentlyFavorited;
        court.setFavorite(isCourtFavorite);

        // Update the favorites string (add or remove the court ID)
        String updatedFavorites = updateFavorites(currentUserFavorites, courtId, isCourtFavorite);

        // Update the database with the new favorites string
        updateFavoritesInDatabase(userId, updatedFavorites);

        // Update the star icon color based on the new isFavorite state
        updateStarIconColor(court, updatedFavorites, listItem);
    }

    // Update the star icon color based on whether the court is in favorites
    private void updateStarIconColor(Court court, String updatedFavorites, View listItem) {
        ImageView starIcon = listItem.findViewById(R.id.star_icon);

        // Check if the court is in the updated favorites string
        boolean isCourtInFavorites = updatedFavorites.contains(String.valueOf(court.getId()));

        // Set the star icon color based on whether the court is in favorites
        court.setFavorite(isCourtInFavorites);

        if (isCourtInFavorites) {
            starIcon.setImageResource(R.drawable.star);
            starIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.yellow));
        } else {
            starIcon.setImageResource(R.drawable.favorites);
            starIcon.setColorFilter(null);
        }
    }

    private String getUserFavoritesFromDatabase(int userId) {
        DataBaseHelper dbHelper = new DataBaseHelper(mContext);
        return dbHelper.getUserFavorites(userId);
    }

    private String updateFavorites(String currentFavorites, int courtId, boolean isFavorite) {

        if (isFavorite) {
            // Add the new court ID to favorites
            if (currentFavorites.isEmpty()) {
                return String.valueOf(courtId);
            } else {
                return currentFavorites + "," + courtId;
            }
        } else {
            // Remove the court ID from favorites
            if (currentFavorites.contains(String.valueOf(courtId))) {
                // Remove the court ID from the list
                String updatedFavorites = currentFavorites.replace(String.valueOf(courtId), "").replaceAll(",,", ",");
                // Remove leading and trailing commas
                return updatedFavorites.replaceAll("^,|,$", "");
            } else {
                // Court ID was not in the favorites, return the current string
                return currentFavorites;
            }
        }
    }

    // Update the user's favorites in the database
    private void updateFavoritesInDatabase(int userId, String updatedFavorites) {
        DataBaseHelper dbHelper = new DataBaseHelper(mContext);
        dbHelper.updateUserFavorites(userId, updatedFavorites);
    }


}