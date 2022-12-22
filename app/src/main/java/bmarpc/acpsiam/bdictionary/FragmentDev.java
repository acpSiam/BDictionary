package bmarpc.acpsiam.bdictionary;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;


public class FragmentDev extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    MaterialButton fb, github, phone, email;
    MaterialCardView bmcButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dev, container, false);

        fb = v.findViewById(R.id.fb_button);
        github = v.findViewById(R.id.github_button);
        phone = v.findViewById(R.id.call_button);
        email = v.findViewById(R.id.email_button);
        bmcButton = v.findViewById(R.id.buy_me_coffee_button_id);


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

        return v;
    }











    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fb_button) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://facebook.com/acp.siam.kb"));
            startActivity(intent);
        }
        else if (id == R.id.github_button) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://github.com/acpSiam"));
            startActivity(intent);
        }
        else if (id == R.id.call_button) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:(+880)1836-652701"));
            startActivity(intent);
        }
        else if (id == R.id.email_button) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","acpsiam@email.com", null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Offline Login Registrer App");
            startActivity(Intent.createChooser(intent, "Choose your prefered E-mail client :"));
        }
        else if (id == R.id.buy_me_coffee_button_id) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.buymeacoffee.com/acpSiam"));
            startActivity(intent);
        }
    }

    @Override
    public boolean onLongClick(View view) {

        int id = view.getId();
        if (id == R.id.fb_button) {
            copyInfo("Facebook", "https://facebook.com/acp.siam.kb");
        }
        else if (id == R.id.github_button) {
            copyInfo("GitHub", "https://github.com/acpSiam");
        }
        else if (id == R.id.call_button) {
            copyInfo("Phone", "(+880)1836-652701");
        }
        else if (id == R.id.email_button) {
            copyInfo("Email", "acpsiam@gmail.com");
        }
        else if (id == R.id.buy_me_coffee_button_id) {
            Toast toast = Toast.makeText(requireActivity(), "It would be a pleasure to drink a coffee with you 😀", Toast.LENGTH_SHORT);
            toast.show();

        }
        return true;
    }

    public void copyInfo(String label, String info){
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, info);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(requireActivity(), "Copied", Toast.LENGTH_SHORT).show();
    }

}