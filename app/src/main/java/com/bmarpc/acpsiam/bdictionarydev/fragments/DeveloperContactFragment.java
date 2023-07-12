package com.bmarpc.acpsiam.bdictionarydev.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bmarpc.acpsiam.bdictionarydev.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;


public class DeveloperContactFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private MaterialButton fb, github, phone, email;
    private MaterialCardView bmcButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_developer_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fb = view.findViewById(R.id.fb_button);
        github = view.findViewById(R.id.github_button);
        phone = view.findViewById(R.id.call_button);
        email = view.findViewById(R.id.email_button);
        bmcButton = view.findViewById(R.id.buy_me_coffee_button_id);

        fb.setOnClickListener(this);
        github.setOnClickListener(this);
        phone.setOnClickListener(this);
        email.setOnClickListener(this);
        bmcButton.setOnClickListener(this);

        fb.setOnLongClickListener(this);
        github.setOnLongClickListener(this);
        phone.setOnLongClickListener(this);
        email.setOnLongClickListener(this);
        bmcButton.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fb_button) {
            openUrl("https://facebook.com/acp.siam.kb");
        } else if (id == R.id.github_button) {
            openUrl("https://github.com/acpSiam");
        } else if (id == R.id.call_button) {
            dialPhoneNumber("(+880)1836-652701");
        } else if (id == R.id.email_button) {
            sendEmail("acpsiam@gmail.com", "BDictionary DEV");
        } else if (id == R.id.buy_me_coffee_button_id) {
            openUrl("https://www.buymeacoffee.com/acpSiam");
        }
    }

    @Override
    public boolean onLongClick(View view) {
        int id = view.getId();
        if (id == R.id.fb_button) {
            copyInfo(requireContext(), "Facebook", "https://facebook.com/acp.siam.kb");
        } else if (id == R.id.github_button) {
            copyInfo(requireContext(), "GitHub", "https://github.com/acpSiam");
        } else if (id == R.id.call_button) {
            copyInfo(requireContext(), "Phone", "(+880)1836-652701");
        } else if (id == R.id.email_button) {
            copyInfo(requireContext(), "Email", "acpsiam@gmail.com");
        } else if (id == R.id.buy_me_coffee_button_id) {
            Toast.makeText(requireContext(), "It would be a pleasure to drink a coffee with you 😀", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void sendEmail(String email, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(intent, "Choose your preferred E-mail client:"));
    }

    private static void copyInfo(Context context, String label, String info) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, info);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
    }
}
