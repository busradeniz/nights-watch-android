package com.busradeniz.nightswatch.ui.violation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.violation.Violation;
import com.busradeniz.nightswatch.service.violation.ViolationGroup;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateViolationActivityFragment extends Fragment {

    private static final int PLACE_PICKER_REQUEST = 3;
    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static String TAG = "CreateViolationActivityFragment";

    private Violation violation;


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.spinner_violation_group)
    AppCompatSpinner spinner_violation_group;
    @Bind(R.id.spinner_violation_danger)
    AppCompatSpinner spinner_violation_danger;
    @Bind(R.id.spinner_violation_frequency)
    AppCompatSpinner spinner_violation_frequency;
    @Bind(R.id.llCustomViolationBase)
    LinearLayout llCustomViolationBase;
    @Bind(R.id.scrollBase)
    ScrollView scrollBase;
    @Bind(R.id.llMediaBase)
    LinearLayout llMediaBase;

    @Bind(R.id.create_violation_txt_location)
    TextView create_violation_txt_location;

    private ArrayList<Uri> mediaListUri;
    private ProgressDialog progressDialog;
    private List<ViolationGroup> violationGroupList;
    private String[] violationGroupSpinnerList;



    public CreateViolationActivityFragment() {
        mediaListUri = new ArrayList<>();
        violation = new Violation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_violation, container, false);
        ButterKnife.bind(this, view);

        // Set up the toolbar.
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.create_violation_screen_title));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        setHasOptionsMenu(true);


        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.danger_level_array,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_violation_frequency.setAdapter(arrayAdapter);
        spinner_violation_danger.setAdapter(arrayAdapter);


        create_violation_txt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(getActivity().getApplicationContext());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        sendGetViolationGroups();
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_violation, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_open_camera:
                openCamera();
                break;
            case R.id.btn_open_library:
                openLibrary();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri imageUri = getImageUri(photo);
            mediaListUri.add(imageUri);
            Log.i(TAG, "Open camera - media List count :" + mediaListUri.size());

            setSelectedImagesView();

        } else if (requestCode == SELECT_PICTURE) {

            if (data != null && data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    mediaListUri.add(uri);
                }

                Log.v(TAG, "Open library - media List count :" + mediaListUri.size());
            }

            setSelectedImagesView();

        }if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(data, getActivity());
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }

            create_violation_txt_location.setText(name.toString() + " " + address.toString());
            Log.i("Adres " , "Adres : " + name + "---" + address + "---" + Html.fromHtml(attributions));
            violation.setAddress(name.toString() + " " + address.toString());
            violation.setLongitude(place.getLatLng().longitude);
            violation.setLatitude(place.getLatLng().latitude);

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openLibrary() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    private void setSelectedImagesView() {

        llMediaBase.removeAllViews();

        for (int i = 0; i < mediaListUri.size(); i++) {
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imgParams.leftMargin = 10;
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(imgParams);

            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(mediaListUri.get(i));
                Drawable yourDrawable = Drawable.createFromStream(inputStream, mediaListUri.get(i).toString());
                Bitmap b = ((BitmapDrawable) yourDrawable).getBitmap();
                Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 240, 240, false);
                imageView.setBackground(new BitmapDrawable(getResources(), bitmapResized));
            } catch (IOException e) {
                e.printStackTrace();
            }

            llMediaBase.addView(imageView);
        }

    }

    public Uri getImageUri(Bitmap inImage) {

        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void sendGetViolationGroups(){
        showProgress();
        ServiceProvider.getViolationService().getViolationGroups()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ViolationGroup>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Log.i(TAG , "getViolationGroups reqest failed :" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<ViolationGroup> violationGroups) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getViolationGroups request success - violation group size :" + violationGroups.size());
                        updateViolationGroupSpinner(violationGroups);
                    }
                });
    }

    private void updateViolationGroupSpinner(List<ViolationGroup> violationGroups){
        violationGroupList = violationGroups;

        violationGroupSpinnerList = new String[violationGroupList.size()];
        for (int i = 0 ; i< violationGroupList.size() ; i ++){
            violationGroupSpinnerList[i] = violationGroupList.get(i).getName();
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_spinner_item,violationGroupSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_violation_group.setAdapter(adapter);


    }
    private void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.progress_title_text), getResources().getString(R.string.progress_message_text), true);
    }

}
