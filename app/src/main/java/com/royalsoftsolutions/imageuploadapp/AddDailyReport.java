package com.royalsoftsolutions.imageuploadapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theartofdev.edmodo.cropper.CropImage;

public class AddDailyReport extends AppCompatActivity {
    ImageButton ibtn_attachment;
   private Context context = AddDailyReport.this;

    private ProgressDialog mDialog;
    private BottomSheetDialog bottomSheetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_add_daily_report );
        ibtn_attachment = (ImageButton) findViewById(R.id.ibtn_attachment);
        mDialog = new ProgressDialog(this);
        mDialog.setCanceledOnTouchOutside(false);

        ibtn_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openBottomSheet();
            }
        });
    }

    private void openBottomSheet() {

        bottomSheetDialog = new BottomSheetDialog (context);
        bottomSheetDialog.setContentView(R.layout.bottomsheetdialog_camera_attachment);

        ImageView imgSelectImage = (ImageView) bottomSheetDialog.findViewById(R.id.imgSelectImage);
        imgSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.startPickImageActivity((Activity) context);
            }
        });

        /*ImageView imgSelectAttachment = (ImageView) bottomSheetDialog.findViewById(R.id.imgSelectAttachment);
        imgSelectAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // openFileSelectDialog();
            }
        });*/

        bottomSheetDialog.show();
    }
}