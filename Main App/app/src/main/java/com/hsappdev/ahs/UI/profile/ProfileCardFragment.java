package com.hsappdev.ahs.UI.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hsappdev.ahs.R;
import com.hsappdev.ahs.util.ImageUtil;
import com.hsappdev.ahs.util.ScreenUtil;

import org.w3c.dom.Text;

public class ProfileCardFragment extends Fragment {
    private static final String TAG = "ProfileCardFragment";

    // Fields
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private int userId;

    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private ImageView barcode;
    private ImageView accountImage;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_card_fragment, container, false);
        // Set vars
        firstNameTextView = view.findViewById(R.id.profile_card_firstname_textView);
        lastNameTextView = view.findViewById(R.id.profile_card_lastname_textView);
        barcode = view.findViewById(R.id.profile_card_barcode);
        accountImage = view.findViewById(R.id.profile_card_profile_img);

        return view;
    }

    public void renderBarcode(){
        // TODO: use the userId and render a barcode
    }

    // GETTERS AND SETTERS
    public void setDetails(String firstName, String lastName, String profilePictureUrl, int userId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureUrl = profilePictureUrl;
        this.userId = userId;

        // Update View
        firstNameTextView.setText(firstName);
        lastNameTextView.setText(lastName);
        ImageUtil.setImageToView(profilePictureUrl, accountImage);
        renderBarcode();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
