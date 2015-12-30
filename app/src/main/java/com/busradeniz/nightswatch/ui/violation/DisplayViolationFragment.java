package com.busradeniz.nightswatch.ui.violation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.busradeniz.nightswatch.R;
import com.busradeniz.nightswatch.service.ServiceProvider;
import com.busradeniz.nightswatch.service.like.Like;
import com.busradeniz.nightswatch.service.violation.ViolationResponse;
import com.busradeniz.nightswatch.util.CircleTransformation;
import com.busradeniz.nightswatch.util.Constants;
import com.busradeniz.nightswatch.util.DateFormatter;
import com.busradeniz.nightswatch.util.NightsWatchApplication;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by busradeniz on 29/12/15.
 */
public class DisplayViolationFragment extends Fragment {

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


    private boolean isLike;
    private ViolationResponse selectedViolation;
    private static String TAG = "DisplayViolationFragment";
    private Like userLike;

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


        if (selectedViolation != null) {
            sendGetViolationLikes();
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


        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_display_violation, menu);
        super.onCreateOptionsMenu(menu, inflater);

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
                                userLike = likes.get(i);
                                setLikeView(true);
                                return;
                            }
                        }
                    }
                });

    }

    private void setLikeView(Boolean like) {
        isLike = like;
        if (like) {
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

    private void sendLikeUnLikeRequest() {
        if (isLike) {
            // send unlike


        } else {
            Like like = new Like();
            like.setId(0);
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
                            userLike = like;
                            setLikeView(true);
                            int count = Integer.parseInt(txtViolationLikeNumber.getText().toString()) + 1;
                            txtViolationLikeNumber.setText("" + count);
                        }
                    });
        }
    }

}
