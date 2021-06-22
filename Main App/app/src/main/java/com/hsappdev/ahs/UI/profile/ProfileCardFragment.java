package com.hsappdev.ahs.UI.profile;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.util.BarcodeDrawable;
import com.hsappdev.ahs.util.ImageUtil;

import java.util.regex.Pattern;

public class ProfileCardFragment extends Fragment {
    private static final String TAG = "ProfileCardFragment";
    private static final Pattern idOfEmail = Pattern.compile("^(\\d{5})@students\\.ausd\\.net$");

    private TextView givenNameTextView;
    private TextView familyNameTextView;
    private ImageView barcodeImage;
    private ImageView accountImage;

    private GoogleSignInClient gsClient;
    private static final int RC_SIGN_IN = 8888;
    private Boolean gsSignedIn = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_card_fragment, container, false);
        // Set vars
        givenNameTextView = view.findViewById(R.id.profile_card_given_name_textView);
        familyNameTextView = view.findViewById(R.id.profile_card_family_name_textView);
        barcodeImage = view.findViewById(R.id.profile_card_barcode_img);
        accountImage = view.findViewById(R.id.profile_card_photo_img);

        // Google OAuth
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gsOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        Context context = getActivity();
        gsClient = GoogleSignIn.getClient(context, gsOptions);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        gsSignedIn = account != null;
        if(gsSignedIn) setDetails(account);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gsSignedIn) {
                    gsClient.signOut();
                    gsSignedIn = false;
                    setDetails(0);
                } else {
                    Intent signInIntent = gsClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                gsSignedIn = account != null;
                if(gsSignedIn) setDetails(account);
            } catch (ApiException error) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w(TAG, "signInResult:failed code=" + error.getStatusCode());
                setDetails(0);
            }
        }
    }

    // GETTERS AND SETTERS
    public void setDetails(String givenName, String familyName, String photoUrl, String userId){
        setGivenName(givenName);
        setFamilyName(familyName);
        setPhotoUrl(photoUrl);
        setUserId(userId);
    }
    public void setDetails(GoogleSignInAccount account){
        setGivenName(account.getGivenName());
        setFamilyName(account.getFamilyName());
        try {
            setPhotoUrl(account.getPhotoUrl().toString());
        } catch (NullPointerException error) {
            Log.d(TAG,"Profile photo does not exist.");
        }
        try {
            setUserId(idOfEmail.matcher(account.getEmail()).group());
        } catch (NullPointerException error) {
            Log.d(TAG,"Email does not exist.");
        } catch (IllegalStateException error) {
            Log.d(TAG, "Email does not contain ID.");
        }
    }
    public void setDetails(int reset) {
        Resources res = getResources();
        setDetails(
                res.getString(R.string.profile_card_default_given_name),
                res.getString(R.string.profile_card_default_family_name),
                res.getString(R.string.profile_card_default_photo_url),
                res.getString(R.string.profile_card_default_user_id)
        );
    }

    public void setGivenName(String givenName) {
        givenNameTextView.setText(givenName);
    }

    public void setFamilyName(String familyName) {
        familyNameTextView.setText(familyName);
    }

    public void setPhotoUrl(String photoUrl) {
        ImageUtil.setCircleImageToView(photoUrl, accountImage);
    }

    public void setUserId(String userId) {
        BarcodeDrawable barcodeCanvas = new BarcodeDrawable(Integer.parseInt(userId));
        barcodeImage.setImageDrawable(barcodeCanvas);
    }
}
