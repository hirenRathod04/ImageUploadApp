package com.royalsoftsolutions.imageuploadapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddDailyReport extends AppCompatActivity {
    ImageButton ibtn_attachment;
    Button btn_add;
    ImageView imageView;

   private Context context = AddDailyReport.this;
private String mMonth1;
private String mDay1;
    private ProgressDialog mDialog;
    private BottomSheetDialog bottomSheetDialog;
    TextView tvGetTodayDate;
    private String fileExtension = "";
    private String attachmentBase64String = "";


    public AddDailyReport() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_add_daily_report );

        btn_add = findViewById ( R.id.btn_add );
        imageView = findViewById ( R.id.image_view );
        ibtn_attachment = (ImageButton) findViewById(R.id.ibtn_attachment);
        tvGetTodayDate = findViewById ( R.id.tvDate );
        mDialog = new ProgressDialog(this);
        mDialog.setCanceledOnTouchOutside(false);

        DisplayDate();
        loadDetails();

        btn_add.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

            }
        } );

        ibtn_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openBottomSheet();
            }
        });
    }

    private void DisplayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date TodayDate = new Date();
        tvGetTodayDate.setText(dateFormat.format(TodayDate));
    }

    private void loadDetails() {


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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //RESULT FROM SELECTED IMAGE
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {

            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            cropRequest(imageUri);



        }
        //RESULT FROM CROP ACTIVITY
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                //showing image.
                    imageView.setImageURI ( result.getUri () );


                try {
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                   // fileExtension = ".jpg";
                    attachmentBase64String = ConvertFileToBase64String.convertBitmapToBase64String(bitmapImage);
                    if (! TextUtils.isEmpty(attachmentBase64String)) {
                        ibtn_attachment.setImageDrawable(getDrawable(R.drawable.ic_description_gray_24dp));
                        //ivRemove.setVisibility(View.VISIBLE);
                        Toast.makeText ( context,"attachmentBase64String_got",
                                Toast.LENGTH_SHORT ).show ();

                        bottomSheetDialog.dismiss();
                    } else {
                        fileExtension = "";
                        attachmentBase64String = "";
                        ibtn_attachment.setImageDrawable(getDrawable(R.drawable.ic_attach_file_gray_24dp));
                       // ivRemove.setVisibility(View.INVISIBLE);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                fileExtension = "";
                attachmentBase64String = "";
                ibtn_attachment.setImageDrawable(getDrawable(R.drawable.ic_attach_file_gray_24dp));
               // ivRemove.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void cropRequest(Uri imageUri) {

        CropImage.activity(imageUri).setGuidelines( CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this);
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (dialog != null) {   //Show dialog if the read permission has been granted.
                    dialog.show();
                }
            } else {
                //Permission has not been granted. Notify the user.
                Common.showToast(context, "Permission is Required for getting list of files");
            }
        }
    }*/

    /*private void loadAddedReport() {

        list = new ArrayList<> ();

        SimpleDateFormat formatter_txt = new SimpleDateFormat("yyyy-MM-dd");
        Date date_txt = new Date();
        final String drdate = formatter_txt.format(date_txt);
        final String umid = preferences.getString("umid", "0");
        String url = ERP.erpWSJSONUrl;
        Map<String, String> params = new HashMap<> ();
        params.put(AppUser.actionCallFunction, "addedreport");
        params.put("umid", umid);
        params.put("drdate", drdate);

        new StringRequestVolley(context, url, params, new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("responseCode").equals("0")) {

                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONArray jsonDailyReport = result.getJSONArray("dailyReport");

                        for (int i = 0; i < jsonDailyReport.length(); i++) {

                            JSONObject object = jsonDailyReport.getJSONObject(i);

                            DailyReport dailyReport = new DailyReport();

                            dailyReport.setSrno(object.getString("srno"));
                            dailyReport.setDrid(object.getString("drid"));
                            dailyReport.setDrdate(object.getString("drdate"));
                            //dailyReport.setDrdetails(object.getString("drdetails").replace("&quot;", ""));
                            dailyReport.setDrdetails(object.getString("drdetails").replace("u20b9", "₹"));
                            dailyReport.setDrattachment(object.getString("drattachment"));
                            dailyReport.setDstatus(object.getString("status"));

                            list.add(dailyReport);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new ToolDailyReportAddedAdapter(ToolDailyReportAddActivity.this, list, ToolDailyReportAddActivity.this);
                list_report.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                mDialog.dismiss();
                Common.showToast(context, errorMessage);
            }
        });
    }*/

  /*  private void checkReport(String action) {

        if (action == "ADD") {
            btn_add.setVisibility(View.INVISIBLE);
        } else if (action == "EDIT") {
            llEditButtonLayout.setVisibility(View.INVISIBLE);
        }

        String umid = preferences.getString("umid", "0");
        String report = edt_report.getText().toString().trim();

        if (umid.equals("0")) {
            Toast.makeText(getApplicationContext(), "Session timeout, Please login again!", Toast.LENGTH_SHORT).show();
            btn_add.setVisibility(View.VISIBLE);
            return;
        }

        if (report.isEmpty()) {

            edt_report.setError("Please enter report.");
            btn_add.setVisibility(View.VISIBLE);
            return;
        }

        String attachmentFileName = "";
        if (!TextUtils.isEmpty(attachmentBase64String)) {
            attachmentFileName = "dailyreport_" + Common.getCurrentDateTimeForFileName() + fileExtension;
        } else {
            attachmentFileName = "";
        }

        addReport(action, umid, drID, report, attachmentBase64String, attachmentFileName);

    }*/

   /* private void addReport(final String action, final String umid, final String drid, final String report, final String doc_image, final String filename) {

        mDialog.setMessage("Please wait...");
        mDialog.show();

        String url = ERP.erpWSJSONUrl;
        Map<String, String> params = new HashMap<>();

        if (action.equals("ADD")) {
            params.put(AppUser.actionCallFunction, "addreport");

        } else if (action.equals("EDIT")) {
            params.put(AppUser.actionCallFunction, "editreport");
            params.put("drid", drid);

        }
        params.put("umid", umid);
        params.put("report", report);
        params.put("uimage", doc_image);
        params.put("filename", filename);

        new StringRequestVolley(context, url, params, new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("responseCode").equals("0")) {

                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        clearData();
                        loadAddedReport();

                    } else {
                        mDialog.dismiss();
                        if (action == "ADD")
                            btn_add.setVisibility(View.VISIBLE);
                        else if (action == "EDIT")
                            llEditButtonLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    mDialog.dismiss();
                    if (action == "ADD")
                        btn_add.setVisibility(View.VISIBLE);
                    else if (action == "EDIT")
                        llEditButtonLayout.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                mDialog.dismiss();
                if (action == "ADD")
                    btn_add.setVisibility(View.VISIBLE);
                else if (action == "EDIT")
                    llEditButtonLayout.setVisibility(View.VISIBLE);
                Common.showToast(context, errorMessage);
            }
        });
    }*/
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            Intent intent = new Intent(ToolDailyReportAddActivity.this, ToolDailyReportActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }*/
/*
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ToolDailyReportAddActivity.this, ToolDailyReportActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }*/

  /*  @Override
    public void onDailyReportEditClickListener(DailyReport dailyReport) {
        mDialog.setMessage("Please wait...");
        mDialog.show();

        drID = dailyReport.getDrid();
        edt_report.setText(dailyReport.getDrdetails());
        String attachmentUrl = dailyReport.getDrattachment();

        if (!TextUtils.isEmpty(attachmentUrl)) {
            fileExtension = attachmentUrl.substring(attachmentUrl.lastIndexOf("."));
            attachmentBase64String = ConvertFileToBase64String.convertBitmapToBase64String(ConvertUrlToFile.convertImageUrlToBitmap(attachmentUrl));
            ibtn_attachment.setImageResource(R.drawable.ic_description_gray_24dp);
            ivRemove.setVisibility(View.VISIBLE);

            btn_add.setVisibility(View.GONE);
            llEditButtonLayout.setVisibility(View.VISIBLE);
            mDialog.dismiss();

        } else {
            ibtn_attachment.setImageResource(R.drawable.ic_attach_file_gray_24dp);
            ivRemove.setVisibility(View.INVISIBLE);
            fileExtension = "";
            attachmentBase64String = "";

            btn_add.setVisibility(View.GONE);
            llEditButtonLayout.setVisibility(View.VISIBLE);
            mDialog.dismiss();
        }
    }*/
}