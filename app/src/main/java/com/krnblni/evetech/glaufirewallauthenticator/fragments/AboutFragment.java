package com.krnblni.evetech.glaufirewallauthenticator.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.krnblni.evetech.glaufirewallauthenticator.R;

public class AboutFragment extends Fragment {

    ImageView linkedInLinkImageView, twitterLinkImageView, facebookLinkImageView, instagramLinkImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        findAllIds(view);

        linkedInLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLinkedInId();
            }
        });

        twitterLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTwitterId();
            }
        });

        facebookLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebookId();
            }
        });

        instagramLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagramId();
            }
        });

        return view;
    }

    private void openInstagramId() {

        Uri uri = Uri.parse("http://instagram.com/_u/" + "krnblni");
        Intent instagramIntent = new Intent(Intent.ACTION_VIEW, uri);
        instagramIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        instagramIntent.setPackage("com.instagram.android");
        try {
            startActivity(instagramIntent);
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/" + "krnblni")));
        }

    }

    private void openFacebookId() {

        Uri uri = Uri.parse("fb://profile/" + "100004179423460");
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, uri);
        facebookIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(facebookIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + "krnblni")));
        }

    }

    private void openTwitterId() {

        Uri uri = Uri.parse("twitter://user?screen_name=" + "krnblni");
        Intent twitterIntent = new Intent(Intent.ACTION_VIEW, uri);
        twitterIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(twitterIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + "krnblni")));
        }

    }

    private void openLinkedInId() {
        Uri uri = Uri.parse("linkedin://add/%@" + "krnblni");
        Intent linkedInIntent = new Intent(Intent.ACTION_VIEW, uri);
        linkedInIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(linkedInIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/in/" + "krnblni")));
        }
    }

    private void findAllIds(View view) {
        linkedInLinkImageView = view.findViewById(R.id.linkedInLinkImageView);
        twitterLinkImageView = view.findViewById(R.id.twitterLinkImageView);
        facebookLinkImageView = view.findViewById(R.id.facebookLinkImageView);
        instagramLinkImageView = view.findViewById(R.id.instagramLinkImageView);
    }
}
