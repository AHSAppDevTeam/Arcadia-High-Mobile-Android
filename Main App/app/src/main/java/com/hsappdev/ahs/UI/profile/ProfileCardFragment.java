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
import com.hsappdev.ahs.util.BarcodeDrawable;
import com.hsappdev.ahs.util.ImageUtil;

public class ProfileCardFragment extends Fragment {
    private static final String TAG = "ProfileCardFragment";

    // Fields
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private int userId;

    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private ImageView barcodeImage;
    private ImageView accountImage;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_card_fragment, container, false);
        // Set vars
        firstNameTextView = view.findViewById(R.id.profile_card_first_name_textView);
        lastNameTextView = view.findViewById(R.id.profile_card_last_name_textView);
        barcodeImage = view.findViewById(R.id.profile_card_barcode_img);
        accountImage = view.findViewById(R.id.profile_card_profile_img);

        setDetails("Giovanna-Xiomara","Carranza-Castellon","https://ahs.app/icon.png",69420);
        return view;
    }


    // GETTERS AND SETTERS
    public void setDetails(String firstName, String lastName, String profilePictureUrl, int userId){
        setFirstName(firstName);
        setLastName(lastName);
        setProfilePictureUrl(profilePictureUrl);
        setUserId(userId);
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        firstNameTextView.setText(firstName);
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
        lastNameTextView.setText(lastName);
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
        ImageUtil.setImageToView(profilePictureUrl, accountImage);
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
        BarcodeDrawable barcodeCanvas = new BarcodeDrawable(userId);
        barcodeImage.setImageDrawable(barcodeCanvas);
    }
}
