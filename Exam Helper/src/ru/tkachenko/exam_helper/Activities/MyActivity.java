package ru.tkachenko.exam_helper.Activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import ru.tkachenko.exam_helper.Cropper.CropImage;
import ru.tkachenko.exam_helper.Cropper.FileStorageHelper;
import ru.tkachenko.exam_helper.R;

import java.io.File;

public class MyActivity extends Activity {
    private static final int PICK_FROM_CAMERA = 1, CROPPED_PICTURE = 2, SPLASH_DISPLAY_LENGHT = 4000;
    ;
    public static Typeface myFont;
    private final String dirName = "StudyMaster";
    private Uri mImageCaptureUri;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splashscreen);

        ImageView myImageView = (ImageView) findViewById(R.id.splashImage);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation myFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        myFadeOutAnimation.setStartOffset(myFadeInAnimation.getDuration());

        AnimationSet myAnimSet = new AnimationSet(true);

        myAnimSet.addAnimation(myFadeInAnimation);
        myAnimSet.addAnimation(myFadeOutAnimation);

        myImageView.startAnimation(myAnimSet);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initActivity();
            }
        }, SPLASH_DISPLAY_LENGHT);

    }

    private void initActivity() {
        this.setContentView(R.layout.main);
        setContentView(R.layout.main);

        FileStorageHelper.setDirFile(getAlbumStorageDir());
        Button button = (Button) findViewById(R.id.captureBtn);
        myFont = Typeface.createFromAsset(getAssets(),
                "fonts/AgentOrange.ttf");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTakePhotoAction();

            }
        });

    }

    public File getAlbumStorageDir() {
        // Get the directory for the app's private pictures directory.
        File file = new File(this.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), dirName);
        if (!file.mkdirs()) {
            Log.e(FileStorageHelper.LOG_FILE_TAG, "Directory not created");
        }
        return file;
    }

    private void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageCaptureUri = Uri.fromFile(FileStorageHelper.createMainFile());

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);


        try {
            intent.putExtra("return-data", false);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                Intent intent = new Intent(this, CropImage.class);
                intent.putExtra("image-path", mImageCaptureUri.getPath());
                startActivityForResult(intent, CROPPED_PICTURE);
                break;
            case CROPPED_PICTURE:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
