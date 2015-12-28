package com.busradeniz.nightswatch.ui.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.signup.SignUpRequest;
import com.busradeniz.nightswatch.service.signup.SignUpResponse;
import com.busradeniz.nightswatch.util.CircleTransformation;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by busradeniz on 25/12/15.
 */
public class EditProfileFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_PICTURE = 2;
    private Bitmap selectedProfilePhoto;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edit_profile_img_user)
    ImageView edit_profile_img_user;
    @Bind(R.id.edit_profile_txt_full_name)
    EditText edit_profile_txt_full_name;
    @Bind(R.id.edit_profile_txt_username)
    EditText edit_profile_txt_username;
    @Bind(R.id.edit_profile_txt_email)
    EditText edit_profile_txt_email;
    @Bind(R.id.edit_profile_txt_bio)
    EditText edit_profile_txt_bio;
    @Bind(R.id.edit_profile_btn_submit)
    AppCompatButton edit_profile_btn_submit;

    public EditProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle(getResources().getString(R.string.profile_txt_edit_rpofile));

        Picasso.with(getActivity())
                .load(Constants.IMAGE_URL).transform(new CircleTransformation()).into(edit_profile_img_user);

        edit_profile_img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionMenu();
            }
        });

        edit_profile_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
        return view;
    }

    private void updateUserInfo(){
        //TODO validation
        sendUpdateUserInfoRequest();
    }

    private void sendUpdateUserInfoRequest(){
        SignUpResponse signUpRequest = new SignUpResponse();
        signUpRequest.setBio("asfoaj agiasjkg asgak");
        signUpRequest.setBirthday("12.12.1989");
        signUpRequest.setEmail("busradeniz89@gmail.com");
        signUpRequest.setFullName("Busra Deniz");
        signUpRequest.setGenderTypeDto("FEMALE");
        signUpRequest.setUsername("test");
        signUpRequest.setId(1);
        ServiceProvider.getUserService().updateUserInfo(NightsWatchApplication.userId , signUpRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignUpResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO hata mesajı
                    }

                    @Override
                    public void onNext(SignUpResponse signUpResponse) {

                    }
                });
    }
    private void openOptionMenu() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.photoOptionTitle)
                .setItems(R.array.photoOptionArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            openCamera();
                        } else {
                            openLibrary();
                        }


                    }
                });

        builder.show();

    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    private void openLibrary() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            selectedProfilePhoto = (Bitmap) data.getExtras().get("data");
            setProfilePhoto(selectedProfilePhoto);
        } else if (requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            try {
                selectedProfilePhoto = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                setProfilePhoto(selectedProfilePhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setProfilePhoto(Bitmap image) {
        CircleTransformation circleTransformation = new CircleTransformation();
        edit_profile_img_user.setImageBitmap(circleTransformation.transform(image));
    }

}
