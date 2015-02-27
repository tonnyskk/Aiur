package com.origin.aiur.activity.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.origin.aiur.BaseActivity;
import com.origin.aiur.R;
import com.origin.aiur.dao.UserDao;
import com.origin.aiur.http.HttpUtils;
import com.origin.aiur.utils.ALogger;
import com.origin.aiur.utils.AppUtils;
import com.origin.aiur.utils.Base64Util;
import com.origin.aiur.vo.User;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

public class ProfileActivity extends BaseActivity implements TextWatcher {
    private TextView userProfileName;
    private TextView userProfileAccount;
    private ImageView userProfileAvatar;

    private TextView userUpdatePwd;
    private View changePasswordContainer;
    private EditText userPasswordOld;
    private EditText userPasswordNew1;
    private EditText userPasswordNew2;
    private TextView passwordButton = null;

    private String[] userAvatarItems;
    private static final String IMAGE_FILE_NAME = "aiur_header_tmp.jpg";
    private static final int IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int RESIZE_REQUEST_CODE = 102;

    enum Actions {
        change_password, update_avatar
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);

        userProfileName = (TextView) this.findViewById(R.id.userProfileName);
        userProfileAccount = (TextView) this.findViewById(R.id.userProfileAccount);

        userAvatarItems = new String[]{
                this.getResources().getString(R.string.text_setup_avatar_local),
                this.getResources().getString(R.string.text_setup_avatar_camera)};

        userProfileAvatar = (ImageView) findViewById(R.id.userProfileAvatar);
        userProfileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setTitle(R.string.text_setup_avatar)
                        .setItems(userAvatarItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                switch (which) {
                                    case 0:
                                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                                        galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            "image/*");
                                        startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
                                        break;
                                    case 1:
                                        if (isExternalStorageExisting()) {
                                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                                        } else {
                                            showToastMessage(ProfileActivity.this.getString(R.string.text_error_no_storage));
                                        }
                                        break;
                                }
                            }
                        })
                        .setNegativeButton(R.string.text_setup_avatar_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });

        changePasswordContainer = findViewById(R.id.changePasswordContainer);
        userUpdatePwd = (TextView)findViewById(R.id.userUpdatePwd);
        userUpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (changePasswordContainer.getVisibility() == View.VISIBLE) {
                    changePasswordContainer.setVisibility(View.GONE);
                } else {
                    changePasswordContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        userPasswordOld = (EditText) findViewById(R.id.changePasswordOld);
        userPasswordOld.addTextChangedListener(this);
        userPasswordNew1 = (EditText) findViewById(R.id.changePasswordNew1);
        userPasswordNew1.addTextChangedListener(this);
        userPasswordNew2 = (EditText) findViewById(R.id.changePasswordNew2);
        userPasswordNew2.addTextChangedListener(this);
        passwordButton = (TextView)findViewById(R.id.btnChange);
        passwordButton.setOnClickListener(this);

        User user = UserDao.getInstance().getCurrentUser();
        if (user != null) {

            userProfileName.setText(user.getNickName());
            userProfileAccount.setText(user.getLoginName());

            Drawable avatar = AppUtils.getAvatar(user.getAvatarData());
            if (avatar != null) {
                userProfileAvatar.setImageDrawable(avatar);
            }
        }
    }

    @Override
    public void performClick(View view) {{
        if (view == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btnChange:
                if (isInputValid()) {
                    HashMap<String, Object> param = new HashMap<String, Object>();
                    param.put("loginName", UserDao.getInstance().getCurrentUser().getLoginName());
                    param.put("password", AppUtils.encryptKey(userPasswordOld.getText().toString()));
                    param.put("passwordNew", AppUtils.encryptKey(userPasswordNew1.getText().toString()));
                    this.postSync(Actions.change_password.name(), param);
                }
                break;
        }
    }

    }

    @Override
    public void onPostExecuteSuccessful(String action, JSONObject response) {
        switch (Actions.valueOf(action)) {
            case change_password:
                showToastMessage(this.getString(R.string.text_common_change_pwd));
                break;
            case update_avatar:
                // Update user identity data
                JSONObject userInfo = AppUtils.getJsonObject(response, "data");
                User loginUser = new User(userInfo);
                UserDao.getInstance().setCurrentUser(loginUser);

                changePasswordContainer.setVisibility(View.GONE);
                userPasswordOld.setText("");
                userPasswordNew1.setText("");
                userPasswordNew2.setText("");
                break;
        }
    }

    @Override
    public void onPostExecuteFailed(String action) {

    }

    @Override
    public String getPath(String action, Object... args) {
        String path = null;
        switch (Actions.valueOf(action)) {
            case change_password:
                path = HttpUtils.user_change_pwd;
                break;
            case update_avatar:
                path = HttpUtils.user_change_avatar;
                break;
        }
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    resizeImage(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    if (isExternalStorageExisting()) {
                        resizeImage(getImageUri());
                    } else {
                        showToastMessage(ProfileActivity.this.getString(R.string.text_error_no_storage));
                    }
                    break;
                case RESIZE_REQUEST_CODE:
                    if (data != null) {
                        showResizeImage(data);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isExternalStorageExisting() {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photoData = extras.getParcelable("data");
            Bitmap photo = AppUtils.toRoundBitmap(photoData, true);
            Drawable drawable = new BitmapDrawable(photo);
            userProfileAvatar.setImageDrawable(drawable);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            byte[] b = stream.toByteArray();
            String avatarData = new String(Base64Util.encode(b));

            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("userID", UserDao.getInstance().getUserId());
            param.put("avatarData", avatarData);
            this.postSync(Actions.update_avatar.name(), param);
        }
    }

    private Uri getImageUri() {
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        String pwdOld = userPasswordOld.getText().toString();
        String pwdNew1 = userPasswordNew1.getText().toString();
        String pwdNew2 = userPasswordNew1.getText().toString();

        if (AppUtils.isEmpty(pwdNew2) || AppUtils.isEmpty(pwdOld) || AppUtils.isEmpty(pwdNew1)) {
            passwordButton.setEnabled(false);
        } else {
            passwordButton.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private boolean isInputValid() {
        String pwdNew1 = userPasswordNew1.getText().toString();
        String pwdNew2 = userPasswordNew2.getText().toString();

        if (AppUtils.isEmpty(pwdNew1) || AppUtils.isEmpty(pwdNew2)) {
            showToastMessage(this.getString(R.string.no_input_value));
            return false;
        }
        if (!pwdNew1.equals(pwdNew2)) {
            showToastMessage(this.getString(R.string.pwd_not_consist));
            return false;
        }
        return true;
    }
}
