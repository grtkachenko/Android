package ru.tkachenko.exam_helper.Cropper;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gtkachenko
 * Date: 04.03.13
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
public class FileStorageHelper {
    public final static String LOG_FILE_TAG = "file";
    private static List<Uri> filesUri = new ArrayList<Uri>();
    private static File dirFile = null;

    public static void setDirFile(File dirFile) {
        FileStorageHelper.dirFile = dirFile;
    }


    public static Uri getFileUri(int index) {
        return filesUri.get(index);
    }

    public static int getNumberOfFiles() {
        return filesUri.size();
    }

    public static File createMainFile() {
        return createFile("main_photo_");
    }

    public static File createTempFile() {
        return createFile("temp_photo_");
    }

    private static File createFile(String filePrefix) {
        File myFile;
        if (isExternalStorageWritable()) {
            myFile = new File(dirFile,
                    filePrefix + String.valueOf(System.currentTimeMillis()) + ".jpg");
        } else {
            myFile = new File(Environment.getExternalStorageDirectory(),
                    filePrefix + String.valueOf(System.currentTimeMillis()) + ".jpg");
        }
        filesUri.add(Uri.fromFile(myFile));
        return myFile;
    }


    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


}
