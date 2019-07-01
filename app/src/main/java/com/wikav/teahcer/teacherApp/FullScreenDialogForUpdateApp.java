package com.wikav.teahcer.teacherApp;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FullScreenDialogForUpdateApp extends DialogFragment {


Button button;
String url;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url=getArguments().getString("url");
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.update_app_layout, container, false);
        button=view.findViewById(R.id.updateBtn);
        button.setOnClickListener(new
                                          View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                 // String url = "https://play.google.com/store/apps/details?id=com.wikav.voulu";
                                                  Intent i = new Intent(Intent.ACTION_VIEW);
                                                  i.setData(Uri.parse(url));
                                                  startActivity(i);
                                              }
                                          });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                getActivity().finish();
                dismiss();
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }



}
