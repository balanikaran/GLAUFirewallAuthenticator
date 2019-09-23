package com.krnblni.evetech.glaufirewallauthenticator.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.krnblni.evetech.glaufirewallauthenticator.R;

public class AboutFragment extends Fragment {

    private ImageView linkedInLinkImageView, facebookLinkImageView, githubLinkImageView;

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
        facebookLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebookId();
            }
        });
        githubLinkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGithubId();
            }
        });
        return view;
    }

    private void openGithubId() {
        Uri uri = Uri.parse("https://www.github.com/krnblni");
        Intent githubIntent = new Intent(Intent.ACTION_VIEW, uri);
        githubIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(githubIntent);
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
        facebookLinkImageView = view.findViewById(R.id.facebookLinkImageView);
        githubLinkImageView = view.findViewById(R.id.githubLinkImageView);
    }
}
