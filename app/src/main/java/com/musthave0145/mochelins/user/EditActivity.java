package com.musthave0145.mochelins.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.api.NetworkClient;
import com.musthave0145.mochelins.api.UserApi;
import com.musthave0145.mochelins.config.Config;
import com.musthave0145.mochelins.meeting.MeetingCreateActivity;
import com.musthave0145.mochelins.model.ResultRes;
import com.musthave0145.mochelins.model.User;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditActivity extends AppCompatActivity {


    ImageView btnBackspace;
    TextView txtSave;

    CircleImageView imgProfile;
    ImageView btnProfileEdit;

    TextInputLayout layoutEmail;
    TextInputLayout layoutNickname;
    TextInputLayout layoutName;
    TextInputLayout layoutPassword1;
    TextInputLayout layoutPassword2;

    TextInputEditText editEmail;
    TextInputEditText editNickName;
    TextInputEditText editName;
    TextInputEditText editPassword1;
    TextInputEditText editPassword2;

    Button btnSave;

    User user;
    File photoFile;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnBackspace = findViewById(R.id.btnBackspace);
        txtSave = findViewById(R.id.txtSave);
        imgProfile = findViewById(R.id.imgProfile);
        btnProfileEdit = findViewById(R.id.btnProfileEdit);
        btnSave = findViewById(R.id.btnSave);

        layoutEmail = findViewById(R.id.layoutEmail);
        layoutNickname = findViewById(R.id.layoutNickname);
        layoutName = findViewById(R.id.layoutName);
        layoutPassword1 = findViewById(R.id.layoutPassword1);
        layoutPassword2 = findViewById(R.id.layoutPassword2);

        editEmail = findViewById(R.id.editEmail);
        editNickName = findViewById(R.id.editNickName);
        editName = findViewById(R.id.editName);
        editPassword1 = findViewById(R.id.editPassword1);
        editPassword2 = findViewById(R.id.editPassword2);

        editEmail.setFocusableInTouchMode(true);
        editNickName.setFocusableInTouchMode(true);
        editName.setFocusableInTouchMode(true);
        editPassword1.setFocusableInTouchMode(true);
        editPassword2.setFocusableInTouchMode(true);


        user = (User) getIntent().getSerializableExtra("user");

        editEmail.setText(user.email);
        editNickName.setText(user.nickname);
        editName.setText(user.name);

        Glide.with(EditActivity.this).load(user.profile)
                        .error(R.drawable.default_profile)
                                .into(imgProfile);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        token = sp.getString(Config.ACCESS_TOKEN, "");

        btnProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_user();
            }
        });

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_user();
            }
        });

        btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void edit_user(){
        String email = editEmail.getText().toString().trim();
        String name = editName.getText().toString().trim();
        String nickname = editNickName.getText().toString().trim();
        String password1 = editPassword1.getText().toString().trim();
        String password2 = editPassword2.getText().toString().trim();

        layoutEmail.setError(null);
        layoutPassword1.setError(null);
        layoutPassword2.setError(null);
        layoutNickname.setError(null);
        layoutName.setError(null);

        Pattern pattern = Patterns.EMAIL_ADDRESS; // 여러 패턴 중에 하나의 패턴 꺼집어낸다.
        if (pattern.matcher(email).matches() == false) {

            layoutEmail.setError("정상적인 이메일의 형식이 아닙니다.");
            editEmail.requestFocus();
            return;
        }

        if(nickname.isEmpty()){
            layoutNickname.setError("닉네임을 입력하셔야 합니다.");
            editNickName.requestFocus();
            return;
        }

        if(name.isEmpty()){
            layoutName.setError("이름을 입력하셔야 합니다.");
            editName.requestFocus();
            return;
        }

        if(!password1.isEmpty() || !password2.isEmpty()){
            if (password1.length() < 6) {
                layoutPassword1.setError("비밀번호는 최소 6자리 이상입니다.");
                editPassword1.requestFocus();
                return;
            }

            if(  !password1.equals(password2)  ){
                layoutPassword2.setError("비밀번호가 서로 다릅니다.");
                editPassword2.requestFocus();
                return;
            }
        }

        showProgress();

        Retrofit retrofit = NetworkClient.getRetrofitClient(EditActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        RequestBody emailBody = RequestBody.create(email,  MediaType.parse("text/plain"));
        RequestBody nicknameBody = RequestBody.create(nickname, MediaType.parse("text/plain"));
        RequestBody nameBody = RequestBody.create(name, MediaType.parse("text/plain"));
        RequestBody passwordBody = RequestBody.create(password1, MediaType.parse("text/plain"));

        Call<ResultRes> call = api.user_edit("Bearer " + token, emailBody, nameBody, nicknameBody);

        if(photoFile != null){
            RequestBody fileBody = RequestBody.create(photoFile, MediaType.parse("image/jpg"));
            MultipartBody.Part profile = MultipartBody.Part.createFormData("profile", photoFile.getName(), fileBody );

            call = api.user_edit("Bearer " + token, emailBody, nameBody, nicknameBody
                    ,profile);

            if(!password1.isEmpty()){
                call = api.user_edit("Bearer " + token, emailBody, nameBody, nicknameBody
                        ,passwordBody ,profile);
            }
        }else if(!password1.isEmpty()){
            call = api.user_edit("Bearer " + token, emailBody, nameBody, nicknameBody
                    ,passwordBody);
        }

        call.enqueue(new Callback<ResultRes>() {
            @Override
            public void onResponse(Call<ResultRes> call, Response<ResultRes> response) {
                dismissProgress();
                if(response.isSuccessful()){

                    setResult(Config.RESTART_NUM);

                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResultRes> call, Throwable t) {
                dismissProgress();
            }
        });

    }


    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
        builder.setTitle(R.string.alert_title);
        builder.setItems(R.array.alert_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    // 첫번째 항목 눌렀을때
                    // 카메라로 사진찍기
                    camera();

                }else if(i == 1){
                    // 두번째 항목 눌렀을때
                    album();
                }
            }
        });
        builder.show();
    }

    private void camera(){
        int permissionCheck = ContextCompat.checkSelfPermission(
                EditActivity.this, android.Manifest.permission.CAMERA);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditActivity.this,
                    new String[]{android.Manifest.permission.CAMERA} ,
                    1000);
            Toast.makeText(EditActivity.this, "카메라 이용 권한이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(i.resolveActivity(EditActivity.this.getPackageManager())  != null  ){

                // 사진의 파일명을 만들기
                String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoFile = getPhotoFile(fileName);

                Uri fileProvider = FileProvider.getUriForFile(EditActivity.this,
                        "com.musthave0145.Mochelins.fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                startActivityForResult(i, 100);

            } else{
                Toast.makeText(EditActivity.this, "이 기기에는 카메라 앱이 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void album(){
        if(checkPermission()){
            displayFileChoose();
        }else{
            requestPermission();
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(EditActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_DENIED){
            return false;
        }else{
            return true;
        }
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(EditActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Log.i("DEBUGGING5", "true");
            Toast.makeText(EditActivity.this, "권한 수락이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
        }else{
            Log.i("DEBUGGING6", "false");
            ActivityCompat.requestPermissions(EditActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
        }
    }

    private void displayFileChoose() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "SELECT IMAGE"), 300);
    }

    private File getPhotoFile(String fileName) {
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try{
            return File.createTempFile(fileName, ".jpg", storageDirectory);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditActivity.this, "권한이 허가 되었습니다.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditActivity.this, "아직 승인하지 않았습니다.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 500: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditActivity.this, "권한이 허가 되었습니다.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditActivity.this, "아직 승인하지 않았습니다.",
                            Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == RESULT_OK){

            Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(photoFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            photo = rotateBitmap(photo, orientation);

            // 압축시킨다. 해상도 낮춰서
            OutputStream os;
            try {
                os = new FileOutputStream(photoFile);
                photo.compress(Bitmap.CompressFormat.JPEG, 50, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }

            photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            imgProfile.setImageBitmap(photo);

            // 네트워크로 데이터 보낸다.



        }else if(requestCode == 300 && resultCode == RESULT_OK && data != null &&
                data.getData() != null){

            Uri albumUri = data.getData( );
            String fileName = getFileName( albumUri );
            try {

                ParcelFileDescriptor parcelFileDescriptor = getContentResolver( ).openFileDescriptor( albumUri, "r" );
                if ( parcelFileDescriptor == null ) return;
                FileInputStream inputStream = new FileInputStream( parcelFileDescriptor.getFileDescriptor( ) );
                photoFile = new File( this.getCacheDir( ), fileName );
                FileOutputStream outputStream = new FileOutputStream( photoFile );
                IOUtils.copy( inputStream, outputStream );

//                //임시파일 생성
//                File file = createImgCacheFile( );
//                String cacheFilePath = file.getAbsolutePath( );


                // 압축시킨다. 해상도 낮춰서
                Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                OutputStream os;
                try {
                    os = new FileOutputStream(photoFile);
                    photo.compress(Bitmap.CompressFormat.JPEG, 60, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }

                imgProfile.setImageBitmap(photo);

            } catch ( Exception e ) {
                e.printStackTrace( );
            }

            // 네트워크로 보낸다.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public String getFileName( Uri uri ) {
        Cursor cursor = getContentResolver( ).query( uri, null, null, null, null );
        try {
            if ( cursor == null ) return null;
            cursor.moveToFirst( );
            @SuppressLint("Range") String fileName = cursor.getString( cursor.getColumnIndex( OpenableColumns.DISPLAY_NAME ) );
            cursor.close( );
            return fileName;

        } catch ( Exception e ) {
            e.printStackTrace( );
            cursor.close( );
            return null;
        }
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    Dialog dialog;

    void showProgress(){
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(new ProgressBar(this));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    void dismissProgress(){
        dialog.dismiss();
    }
}