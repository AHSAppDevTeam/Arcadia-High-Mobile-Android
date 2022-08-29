package com.hsappdev.ahs.UI.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.hsappdev.ahs.R;
import com.hsappdev.ahs.nfc.NfcHandlerActivity;
import com.hsappdev.ahs.util.BarcodeDrawable;
import com.hsappdev.ahs.util.Helper;
import com.hsappdev.ahs.util.ImageUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileCardFragment extends Fragment {
    private static final String TAG = "ProfileCardFragment";
    private static final Pattern userIdPattern = Pattern.compile("^(\\d{5})@students\\.ausd\\.net$");

    private static final String photoUrlID = "PHOTO_URL_ID";


    private TextView givenNameTextView;
    private TextView familyNameTextView;
    private TextView warningTextView;
    private ImageView accountImage;

    private boolean isEditable = true;
    private boolean isSupported = false;

    private GoogleSignInClient gsClient;
    private static final int RC_SIGN_IN = 8888;
    private Boolean gsSignedIn = false;

    private final BarcodeDrawable barcodeCanvas = new BarcodeDrawable();

    public ProfileCardFragment() {
    }

    public ProfileCardFragment(boolean isEditable, boolean isSupported) {
        this.isEditable = isEditable;
        this.isSupported = isSupported;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_card_fragment, container, false);

        // Set vars
        givenNameTextView = view.findViewById(R.id.profile_card_given_name_textView);
        familyNameTextView = view.findViewById(R.id.profile_card_family_name_textView);
        warningTextView = view.findViewById(R.id.profile_card_email_warning);
        accountImage = view.findViewById(R.id.profile_card_photo_img);

        // reset card always
        setDetails();

        // Google OAuth
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gsOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                //.setHostedDomain("students.ausd.net")
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        Context context = getActivity();
        gsClient = GoogleSignIn.getClient(context, gsOptions);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        gsSignedIn = account != null;
        if(gsSignedIn) setDetails(account);

        if(isEditable) view.setOnClickListener(v -> {
                if (gsSignedIn) {
//                Don't sign out user ever
                    gsClient.signOut();
                    gsSignedIn = false;
                    setDetails();
                } else {
                    Intent signInIntent = gsClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
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
                setDetails();
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

        Uri photoUrl = account.getPhotoUrl();
        setPhotoUrl(photoUrl == null ? "https://ahs.app/icon.png" : photoUrl.toString());

        Matcher userIdMatcher = userIdPattern.matcher(account.getEmail());
        if(userIdMatcher.matches()) setUserId(userIdMatcher.group(1));
        else setUserId();
    }
    public void setDetails() {
        Resources res = getResources();
        if(isEditable)
        setDetails(
                res.getString(R.string.profile_card_default_given_name),
                res.getString(R.string.profile_card_default_family_name),
                res.getString(R.string.profile_card_default_photo_url),
                res.getString(R.string.profile_card_default_user_id)
        );
        else setDetails(
                res.getString(R.string.profile_card_default_given_name),
                "",
                res.getString(R.string.profile_card_default_photo_url),
                res.getString(R.string.profile_card_default_user_id)
        );
        setUserId();
    }

    public void setGivenName(String givenName) {
         givenNameTextView.setText(givenName);
    }

    public void setFamilyName(String familyName) {
         familyNameTextView.setText(familyName);
    }

    public void setPhotoUrl(String photoUrl) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(photoUrlID, photoUrl);
        editor.apply();
         ImageUtil.setCircleImageToView(photoUrl, accountImage);
    }

    public void setUserId(String userId) {
        warningTextView.setVisibility(View.INVISIBLE);
        setUserId();
        int userIdInt = -1;
        if(Helper.isStringAnInteger(userId)) {
            userIdInt = Integer.parseInt(userId);
        }
        storeUserId(userIdInt);
    }

    private void storeUserId(int userIdInt) {
        // store the userID in SharedPref
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(NfcHandlerActivity.SHARED_PREF_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(NfcHandlerActivity.SHARED_PREF_ID_DATA_KEY, userIdInt);
        editor.apply();
    }

    public void setUserId() {
        // show warning

        warningTextView.setVisibility(View.VISIBLE);
        if(!isSupported){
            warningTextView.setText("⚠️ NFC is not supported");
        } else {
            if (isEditable) {
                warningTextView.setText("⚠️ Login with your student email\n to use the ID card.");
            } else {
                warningTextView.setText("⚠️ Go to Your Profile to sign in");
            }
        }
        storeUserId(-1); // aka no user id
    }
}