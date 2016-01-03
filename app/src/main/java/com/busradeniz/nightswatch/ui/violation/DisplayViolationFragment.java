package com.busradeniz.nightswatch.ui.violation;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.Response;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.like.Like;
import com.busradeniz.nightswatch.service.violation.ViolationResponse;
import com.busradeniz.nightswatch.service.watch.Watch;
import com.busradeniz.nightswatch.util.CircleTransformation;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.DateFormatter;
import com.busradeniz.nightswatch.util.MediaUtil;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

/**
 * Created by busradeniz on 29/12/15.
 */
public class DisplayViolationFragment extends Fragment {

    private static String TAG = "DisplayViolationFragment";
    private static int RESULT_CODE  =1;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.display_violation_txt_violation_title)
    TextView display_violation_txt_violation_title;
    @Bind(R.id.display_violation_txt_violation_group)
    TextView display_violation_txt_violation_group;
    @Bind(R.id.display_violation_txt_location)
    TextView display_violation_txt_location;
    @Bind(R.id.display_violation_txt_date)
    TextView display_violation_txt_date;
    @Bind(R.id.display_violation_txt_status)
    TextView display_violation_txt_status;
    @Bind(R.id.llMediaBase)
    LinearLayout llMediaBase;
    @Bind(R.id.display_violation_danger_level)
    TextView display_violation_danger_level;
    @Bind(R.id.display_violation_frequency_level)
    TextView display_violation_frequency_level;
    @Bind(R.id.display_violation_description)
    TextView display_violation_description;
    @Bind(R.id.display_violation_tags)
    TextView display_violation_tags;
    @Bind(R.id.txtViolationLikeNumber)
    TextView txtViolationLikeNumber;
    @Bind(R.id.txtViolationCommentNumber)
    TextView txtViolationCommentNumber;
    @Bind(R.id.txtViolationFollowerNumber)
    TextView txtViolationFollowerNumber;
    @Bind(R.id.imgLike)
    ImageView imgLike;
    @Bind(R.id.imgFollower)
    ImageView imgFollower;

    private ViolationResponse selectedViolation;
    private Like userLike = null;
    private Watch userWatch = null;

    public void setSelectedViolation(ViolationResponse selectedViolation) {
        this.selectedViolation = selectedViolation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_display_violation, container, false);
        ButterKnife.bind(this, view);

        // Set up the toolbar.
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.display_violation_screen_title));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        setHasOptionsMenu(true);

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLikeUnLikeRequest();
            }
        });
        imgFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWatchUnwatchRequest();
            }
        });

        setScreen();

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_display_violation, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                openEditScreen();
                break;
            default:
                return super.onOptionsItemSelected(item);

        }

        return true;
    }

    private void openEditScreen(){
        NightsWatchApplication.selectedViolation = selectedViolation;
        Intent intent = new Intent(getActivity(), EditViolationActivity.class);
        startActivityForResult(intent, RESULT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE) {
            selectedViolation = NightsWatchApplication.selectedViolation;
            setScreen();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void setViolationImage() {
        for (int i = 0; i < selectedViolation.getMedias().size(); i++) {
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imgParams.leftMargin = 10;
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(imgParams);
            Log.i(TAG, "Downloading " + selectedViolation.getMedias().get(i).getUrl());
            Picasso.with(getActivity()).load(selectedViolation.getMedias().get(i).getUrl()).resize(240, 240).into(imageView);

            llMediaBase.addView(imageView);
        }
    }

    private void sendGetViolationLikes() {
        ServiceProvider.getViolationService().getViolationLikes(selectedViolation.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Like>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getViolationLikes completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "getViolationLikes failed" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<Like> likes) {
                        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
                        String username = preferences.getString("username", "");

                        for (int i = 0; i < likes.size(); i++) {
                            if (likes.get(i).getUsername().equals(username)) {
                                Log.i(TAG, "like id :" + likes.get(i).getId() + "");
                                setLikeView(likes.get(i));
                                return;
                            }
                        }
                    }
                });

    }

    private void sendGetViolationWatches() {
        ServiceProvider.getViolationService().getViolationWatches(selectedViolation.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Watch>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "getViolationWatches completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "getViolationWatches failed" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<Watch> watches) {
                        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
                        String username = preferences.getString("username", "");

                        for (int i = 0; i < watches.size(); i++) {
                            if (watches.get(i).getUsername().equals(username)) {
                                Log.i(TAG, "watch id :" + watches.get(i).getId() + "");
                                setWatchView(watches.get(i));
                                return;
                            }
                        }
                    }
                });

    }



    private void setLikeView(Like like) {
        userLike = like;
        if (userLike != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imgLike.setBackground(getResources().getDrawable(R.drawable.like_blue, NightsWatchApplication.context.getTheme()));
            } else {
                imgLike.setBackground(getResources().getDrawable(R.drawable.like_blue));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imgLike.setBackground(getResources().getDrawable(R.drawable.like, NightsWatchApplication.context.getTheme()));
            } else {
                imgLike.setBackground(getResources().getDrawable(R.drawable.like));
            }
        }


    }


    private void setWatchView(Watch watch) {
        userWatch = watch;
        if (userWatch != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imgFollower.setBackground(getResources().getDrawable(R.drawable.follow_blue, NightsWatchApplication.context.getTheme()));
            } else {
                imgFollower.setBackground(getResources().getDrawable(R.drawable.follow_blue));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imgFollower.setBackground(getResources().getDrawable(R.drawable.follow, NightsWatchApplication.context.getTheme()));
            } else {
                imgFollower.setBackground(getResources().getDrawable(R.drawable.follow));
            }
        }


    }


    private void sendLikeUnLikeRequest() {
        if (userLike != null) {
            ServiceProvider.getLikeService().unlike(userLike.getId())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Log.i(TAG, "unlike failed :" + e.getLocalizedMessage());

                        }

                        @Override
                        public void onNext(Response response) {
                            Log.i(TAG, "unlike success");
                            setLikeView(null);
                            int count = Integer.parseInt(txtViolationLikeNumber.getText().toString()) -1;
                            txtViolationLikeNumber.setText("" + count);
                        }
                    });


        } else {
            Like like = new Like();
            like.setLikeDate(SystemClock.currentThreadTimeMillis());
            like.setUsername(NightsWatchApplication.username);
            like.setViolationId(selectedViolation.getId());
            ServiceProvider.getLikeService().like(like)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Like>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(TAG, "like failed" + e.getLocalizedMessage());
                        }

                        @Override
                        public void onNext(Like like) {
                            Log.i(TAG, "like success");
                            setLikeView(like);
                            int count = Integer.parseInt(txtViolationLikeNumber.getText().toString()) + 1;
                            txtViolationLikeNumber.setText("" + count);
                        }
                    });
        }
    }

    private void sendWatchUnwatchRequest(){

        if (userWatch != null){
            ServiceProvider.getWatchService().unWatch(userWatch.getId())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Log.i(TAG, "unwatch failed" + e.getLocalizedMessage());

                        }

                        @Override
                        public void onNext(Response response) {
                            Log.i(TAG, "unwatch success");
                            setWatchView(null);
                            int count = Integer.parseInt(txtViolationFollowerNumber.getText().toString()) -1;
                            txtViolationFollowerNumber.setText("" + count);
                        }
                    });


        }else {
            Watch watch = new Watch();
            watch.setViolationId(selectedViolation.getId());
            watch.setUsername(NightsWatchApplication.username);

            ServiceProvider.getWatchService().watch(watch)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Watch>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(TAG, "watch failed" + e.getLocalizedMessage());

                        }

                        @Override
                        public void onNext(Watch watch) {
                            Log.i(TAG, "watch success");
                            setWatchView(watch);
                            int count = Integer.parseInt(txtViolationFollowerNumber.getText().toString()) + 1;
                            txtViolationFollowerNumber.setText("" + count);
                        }
                    });
        }
    }

    private void setScreen(){
        if (selectedViolation != null) {
            sendGetViolationLikes();
            sendGetViolationWatches();
            setViolationImage();
            display_violation_txt_violation_title.setText(selectedViolation.getTitle());
            display_violation_txt_violation_group.setText(" " + selectedViolation.getViolationGroupName());
            display_violation_txt_location.setText(selectedViolation.getAddress());
            display_violation_txt_date.setText(" " + DateFormatter.dateFormatToString(selectedViolation.getViolationDate()));
            display_violation_txt_status.setText(" " + selectedViolation.getViolationStatus());
            display_violation_frequency_level.setText(" " + selectedViolation.getFrequencyLevel());
            display_violation_danger_level.setText(" " + selectedViolation.getDangerLevel());
            display_violation_description.setText(selectedViolation.getDescription());
            txtViolationCommentNumber.setText("" + selectedViolation.getCommentCount());
            txtViolationFollowerNumber.setText("" + selectedViolation.getUserWatchCount());
            txtViolationLikeNumber.setText("" + selectedViolation.getUserLikeCount());

            String tag = "";
            for (int i = 0; i < selectedViolation.getTags().size(); i++) {
                if (i == 0) {
                    tag = selectedViolation.getTags().get(i);
                } else {
                    tag = tag + ", " + selectedViolation.getTags().get(i);
                }
            }
            display_violation_tags.setText(tag);

        }
    }
}
