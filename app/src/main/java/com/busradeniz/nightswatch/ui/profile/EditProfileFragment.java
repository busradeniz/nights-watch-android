package com.busradeniz.nightswatch.ui.profile;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.fileupload.Media;
import com.busradeniz.nightswatch.service.signup.SignUpRequest;
import com.busradeniz.nightswatch.service.signup.SignUpResponse;
import com.busradeniz.nightswatch.util.CircleTransformation;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.MediaUtil;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

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
    private static String TAG = "EditProfileFragment";
    private Uri selectedUri;


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

    private Media selectedMedia;
    private ProgressDialog progressDialog;

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

        setScreen();
        edit_profile_img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOptionMenu();
            }
        });
        edit_profile_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfileImage();
            }
        });
        return view;
    }


    private void uploadProfileImage() {
        showProgress();

        if (selectedUri == null) {
            sendUpdateUserInfoRequest();
            return;
        }
        File file = new File(MediaUtil.getRealPathFromURI(getActivity(), selectedUri));
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("file\"; filename=\"" + UUID.randomUUID().toString().replaceAll("-", "") + ".jpg", requestBody);

        ServiceProvider.getFileUploadService().upload("IMAGE", map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Media>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "upload failed : " + e.getLocalizedMessage());
                        progressDialog.dismiss();
                        Toast.makeText(NightsWatchApplication.context, getString(R.string.edit_profile_photo_fail_text), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Media media) {
                        selectedMedia = media;
                        Log.i(TAG, "upload sucess! ");
                        sendUpdateUserInfoRequest();
                    }
                });

    }

    private void sendUpdateUserInfoRequest() {
        if (!validate()) {
            Toast.makeText(NightsWatchApplication.context, getString(R.string.edit_profile__info_fail_text), Toast.LENGTH_LONG).show();
            return;
        }

        SignUpResponse signUpRequest = new SignUpResponse();
        signUpRequest.setFullName(edit_profile_txt_full_name.getText().toString());
        signUpRequest.setUsername(edit_profile_txt_username.getText().toString());
        signUpRequest.setBio(edit_profile_txt_bio.getText().toString());
        signUpRequest.setEmail(edit_profile_txt_email.getText().toString());

        if (selectedMedia != null){
            Log.i(TAG, "selectedMedia is not null - media id :" + selectedMedia.getId() + " - media url : " + selectedMedia.getUrl() + "  media filename :" + selectedMedia.getFileName());
            signUpRequest.setPhoto(selectedMedia);
        }

        ServiceProvider.getUserService().updateUserInfo(NightsWatchApplication.userId, signUpRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignUpResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "updateUserInfo failed : " + e.getLocalizedMessage());
                        progressDialog.dismiss();
                        Toast.makeText(NightsWatchApplication.context, getString(R.string.edit_profile_fail_text), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(SignUpResponse signUpResponse) {
                        Log.i(TAG, "updateUserInfo sucess! ");
                        if (signUpResponse != null) {
                            NightsWatchApplication.user = signUpResponse;
                        }
                        progressDialog.dismiss();
                        Toast.makeText(NightsWatchApplication.context, getString(R.string.edit_profile_success_text), Toast.LENGTH_LONG).show();
                    }


                });
    }

    private boolean validate() {

        String fullName = edit_profile_txt_full_name.getText().toString();
        String username = edit_profile_txt_username.getText().toString();
        String bio = edit_profile_txt_bio.getText().toString();
        String email = edit_profile_txt_email.getText().toString();

        if (fullName.isEmpty() || username.isEmpty() || bio.isEmpty() || email.isEmpty()) {
            return false;
        }
        return true;
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

            if (data != null && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                selectedUri = MediaUtil.getImageUri(getActivity(), photo);
                setProfilePhoto(photo);
            }

        } else if (requestCode == SELECT_PICTURE) {
            if (data != null) {
                selectedUri = data.getData();
                try {
                    setProfilePhoto(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setProfilePhoto(Bitmap image) {
        CircleTransformation circleTransformation = new CircleTransformation();
        edit_profile_img_user.setImageBitmap(circleTransformation.transform(image));
    }

    private void setScreen() {
        if (NightsWatchApplication.user != null) {
            edit_profile_txt_full_name.setText(NightsWatchApplication.user.getFullName());
            edit_profile_txt_username.setText(NightsWatchApplication.user.getUsername());
            edit_profile_txt_email.setText(NightsWatchApplication.user.getEmail());
            edit_profile_txt_bio.setText(NightsWatchApplication.user.getBio());


            if (NightsWatchApplication.user.getPhoto() != null && NightsWatchApplication.user.getPhoto().getUrl().length() > 0) {
                Picasso.with(getActivity())
                        .load(NightsWatchApplication.user.getPhoto().getUrl()).resize(300, 300).transform(new CircleTransformation()).into(edit_profile_img_user);
            } else {
                edit_profile_img_user.setBackground(getActivity().getDrawable(R.drawable.user));
            }

        }
    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.progress_title_text), getResources().getString(R.string.progress_message_text), true);
    }

}
