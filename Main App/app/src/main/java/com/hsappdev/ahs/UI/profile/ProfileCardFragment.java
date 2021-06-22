package com.hsappdev.ahs.UI.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.util.BarcodeDrawable;
import com.hsappdev.ahs.util.ImageUtil;

import java.util.regex.Pattern;

public class ProfileCardFragment extends Fragment {
    private static final String TAG = "ProfileCardFragment";
    private static final Pattern idOfEmail = Pattern.compile("\\b(\\d{5})@students\\.ausd\\.net\\b");

    // Fields
    private String givenName;
    private String familyName;
    private String PhotoUrl;
    private int userId;

    private TextView givenNameTextView;
    private TextView familyNameTextView;
    private ImageView barcodeImage;
    private ImageView accountImage;

    private Context context;

    private GoogleSignInClient client;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_card_fragment, container, false);
        // Set vars
        givenNameTextView = view.findViewById(R.id.profile_card_given_name_textView);
        familyNameTextView = view.findViewById(R.id.profile_card_family_name_textView);
        barcodeImage = view.findViewById(R.id.profile_card_barcode_img);
        accountImage = view.findViewById(R.id.profile_card_photo_img);

        context = getActivity();
        // Google OAuth
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        client = GoogleSignIn.getClient(context, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);

        if ( account != null ) {
            setDetails(account);
        } else {
            setDetails("Giovanna-Xiomara","Carranza-Castellon","https://ahs.app/icon.png",69420);
        }

        view.setOnClickListener(new View.OnClickListener() {
            private static final int RC_SIGN_IN = 42;

            @Override
            public void onClick(View v)
            {
                Intent signInIntent = client.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        return view;
    }
    
    // GETTERS AND SETTERS
    public void setDetails(String givenName, String familyName, String profilePictureUrl, int userId){
        setGivenName(givenName);
        setFamilyName(familyName);
        setPhotoUrl(profilePictureUrl);
        setUserId(userId);
    }
    public void setDetails(GoogleSignInAccount account){
        setGivenName(account.getGivenName());
        setFamilyName(account.getFamilyName());
        setPhotoUrl(account.getPhotoUrl().toString());
        setUserId(Integer.parseInt(idOfEmail.matcher(account.getEmail()).toMatchResult().group()));
    }

    public String getGivenName() {
        return givenName;
    }
    public void setGivenName(String givenName) {
        this.givenName = givenName;
        givenNameTextView.setText(givenName);
    }

    public String getFamilyName() {
        return familyName;
    }
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
        familyNameTextView.setText(familyName);
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }
    public void setPhotoUrl(String PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
        ImageUtil.setImageToView(PhotoUrl, accountImage);
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
