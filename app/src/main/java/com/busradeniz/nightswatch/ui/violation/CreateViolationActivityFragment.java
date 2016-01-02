package com.busradeniz.nightswatch.ui.violation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.*;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.fileupload.Media;
import com.busradeniz.nightswatch.service.violation.CreateViolationRequest;
import com.busradeniz.nightswatch.service.violation.ViolationGroup;
import com.busradeniz.nightswatch.service.violation.ViolationResponse;
import com.busradeniz.nightswatch.util.AlertDialog;
import com.busradeniz.nightswatch.util.MediaUtil;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateViolationActivityFragment extends Fragment {


    private static final int SELECT_PICTURE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int PLACE_PICKER_REQUEST = 3;
    private static String TAG = "CreateViolationActivityFragment";

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
    @Bind(R.id.create_violation_txt_title)
    TextView create_violation_txt_title;
    @Bind(R.id.create_violation_txt_location)
    TextView create_violation_txt_location;
    @Bind(R.id.create_violation_txt_desc)
    TextView create_violation_txt_desc;
    @Bind(R.id.create_violation_txt_tag)
    TextView create_violation_txt_tag;
    @Bind(R.id.create_violation_btn_submit)
    AppCompatButton create_violation_btn_submit;


    private ArrayList<Uri> mediaListUri;
    private List<ViolationGroup> violationGroupList;
    private String[] violationGroupSpinnerList;
    private CreateViolationRequest violationRequest;
    private String[] dangerLevelList;
    private ProgressDialog progressDialog;


    public CreateViolationActivityFragment() {
        mediaListUri = new ArrayList<>();
        violationRequest = new CreateViolationRequest();
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


        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.danger_level_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_violation_frequency.setAdapter(arrayAdapter);
        spinner_violation_danger.setAdapter(arrayAdapter);
        spinner_violation_frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                violationRequest.setFrequencyLevel(dangerLevelList[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_violation_danger.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                violationRequest.setDangerLevel(dangerLevelList[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dangerLevelList = getResources().getStringArray(R.array.danger_level_array);
        violationRequest.setDangerLevel(dangerLevelList[0]);
        violationRequest.setFrequencyLevel(dangerLevelList[0]);

        create_violation_txt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlacePickerView();
            }
        });


        create_violation_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUploadMediaRequests();
            }
        });
        sendGetViolationGroupsRequest();
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

            if (data != null && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri imageUri = MediaUtil.getImageUri(getActivity(), photo);
                mediaListUri.add(imageUri);
                Log.i(TAG, "Open camera - media List count :" + mediaListUri.size());

                setSelectedImagesView();
            }

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
        }
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            setViolationLocation(data);
        } else {
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

    private void openPlacePickerView() {
        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(getActivity().getApplicationContext());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
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

    private void sendGetViolationGroupsRequest() {
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
                        Log.i(TAG, "getViolationGroups reqest failed :" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<ViolationGroup> violationGroups) {
                        progressDialog.dismiss();
                        Log.i(TAG, "getViolationGroups request success - violationResponse group size :" + violationGroups.size());
                        setViolationGroupSpinnerAdapter(violationGroups);
                    }
                });
    }

    private void setViolationGroupSpinnerAdapter(List<ViolationGroup> violationGroups) {
        if (violationGroups.size() > 0) {
            violationGroupList = violationGroups;

            violationGroupSpinnerList = new String[violationGroupList.size()];
            for (int i = 0; i < violationGroupList.size(); i++) {
                violationGroupSpinnerList[i] = violationGroupList.get(i).getName();
            }

            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, violationGroupSpinnerList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_violation_group.setAdapter(adapter);

            spinner_violation_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    violationRequest.setViolationGroupName(violationGroupList.get(position).getName());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // set default 0
            violationRequest.setViolationGroupName(violationGroupList.get(0).getName());
        }
    }

    private void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.progress_title_text), getResources().getString(R.string.progress_message_text), true);
    }

    private void setViolationLocation(Intent data) {
        final Place place = PlacePicker.getPlace(data, getActivity());
        final CharSequence name = place.getName();
        final CharSequence address = place.getAddress();
        String attributions = PlacePicker.getAttributions(data);
        if (attributions == null) {
            attributions = "";
        }

        create_violation_txt_location.setText(name.toString() + " " + address.toString());
        Log.i(TAG, "Adress from place picket : " + name + "---" + address + "---" + Html.fromHtml(attributions));
        violationRequest.setAddress(name.toString() + " " + address.toString());
        violationRequest.setLongitude(place.getLatLng().longitude);
        violationRequest.setLatitude(place.getLatLng().latitude);
    }


    private void sendUploadMediaRequests() {

        if (!isValidate()) {
            AlertDialog.showAlertWithPositiveButton(getActivity(), "Warning !", "Please fill fields that are marked with *");
            return;
        }

        showProgress();
        ArrayList<Observable<Media>> mediaObservables = new ArrayList<>();
        for (int i = 0; i < mediaListUri.size(); i++) {

            File file = new File(MediaUtil.getRealPathFromURI(getActivity(), mediaListUri.get(i)));
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("file\"; filename=\"" + UUID.randomUUID().toString().replaceAll("-", "") + ".jpg", requestBody);

            Observable<Media> observable = ServiceProvider.getFileUploadService().upload("IMAGE", map);
            mediaObservables.add(observable);
        }


        Observable.combineLatest(mediaObservables, new FuncN<ArrayList<Media>>() {

            @Override
            public ArrayList<Media> call(Object... args) {
                ArrayList<Media> medias = new ArrayList<>();
                for (Object obje : args) {
                    medias.add((Media) obje);
                }
                return medias;
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Media>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "photo combine on completed");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "photo combine least failed : " + e.getLocalizedMessage());
                        progressDialog.dismiss();
                        AlertDialog.showAlertWithPositiveButton(getActivity(), "Create Violation Failed !", "An error occured while uploading medias, please try again !");
                    }

                    @Override
                    public void onNext(ArrayList<Media> medias) {
                        Log.i(TAG, "photo combine least success : " + medias.size());
                        sendCreateViolationRequest(medias);
                    }
                });
    }


    private void sendCreateViolationRequest(final ArrayList<Media> medias) {
        //prepare violationRequest
        violationRequest.setTitle(create_violation_txt_title.getText().toString());
        violationRequest.setViolationStatus("NEW");
        if (create_violation_txt_desc.getText().toString().length() > 0) {
            violationRequest.setDescription(create_violation_txt_desc.getText().toString());
        }

        if (create_violation_txt_tag.getText().toString().length() > 0) {
            String[] tags = create_violation_txt_tag.getText().toString().split(",");
            violationRequest.setTags(tags);
        }

        //send create violation requests
        ServiceProvider.getViolationService().createNewViolation(violationRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ViolationResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "createNewViolation onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "createNewViolation onError : " + e.getLocalizedMessage());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(ViolationResponse violationResponse) {
                        Log.i(TAG, "createNewViolation success");
                        sendAddMediaToViolationRequest(medias, violationResponse.getId());
                    }
                });
    }


    private void sendAddMediaToViolationRequest(ArrayList<Media> medias, final int violationId) {
        ArrayList<Observable<ViolationResponse>> observables = new ArrayList<>();
        for (int i = 0; i < medias.size(); i++) {
            Observable<ViolationResponse> observable = ServiceProvider.getViolationService().addMediaToViolation(violationId, medias.get(i));
            observables.add(observable);
        }

        Observable.combineLatest(observables, new FuncN<ArrayList<ViolationResponse>>() {

            @Override
            public ArrayList<ViolationResponse> call(Object... args) {
                ArrayList<ViolationResponse> violations = new ArrayList<>();
                for (Object obje : args) {
                    violations.add((ViolationResponse) obje);
                }
                return violations;
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<ViolationResponse>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "photo combine on completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "photo add onfailed : " + e.getLocalizedMessage());
                        progressDialog.dismiss();
                        AlertDialog.showAlertWithPositiveButton(getActivity(), "Create Violation Failed !", "An error occured while uploading medias, please try again !");
                    }

                    @Override
                    public void onNext(ArrayList<ViolationResponse> violationResponses) {
                        Log.i(TAG, "photo add success : " + violationResponses.size());
                        progressDialog.dismiss();
                        showSuccessAlertDialog();
                    }
                });
    }

    private boolean isValidate() {
        if (create_violation_txt_title.getText().toString().length() > 0 && violationRequest.getLatitude() > 0
                && violationRequest.getLongitude() != 0 && violationRequest.getAddress().length() != 0 && mediaListUri.size() > 0) {
            return true;
        }

        return false;
    }

    private void showSuccessAlertDialog() {

        new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setTitle("Create Violation Succeed!")
                .setMessage("Violation creates successfully")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                })
                .show();


    }
}

