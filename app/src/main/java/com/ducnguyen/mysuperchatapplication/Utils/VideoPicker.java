package com.ducnguyen.mysuperchatapplication.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.ducnguyen.mysuperchatapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoPicker {

    public static Intent getPickVideoIntent(Context context) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        Intent recordVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        recordVideoIntent.putExtra("return-data", true);
        recordVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(context)));
        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, recordVideoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    context.getString(R.string.pick_video_intent_text));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    public static File getTempFile(Context context) {
        File videoFile = new File(context.getExternalCacheDir(), "tempVideo");
        videoFile.getParentFile().mkdirs();
        return videoFile;
    }

    public static String getVideoFromResult(Context context, int resultCode,
                                            Intent videoReturnedIntent) {
        File videoFile = getTempFile(context);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedVideo;
            boolean isCamera = (videoReturnedIntent == null ||
                    videoReturnedIntent.getData() == null);
            if (isCamera) {     /** CAMERA **/
                selectedVideo = Uri.fromFile(videoFile);
            } else {            /** ALBUM **/
                selectedVideo = videoReturnedIntent.getData();
            }
            return generatePath(selectedVideo,context);
        }
        return null;
    }

    public static Uri getVideoUriFromResult(Context context, int resultCode,
                                            Intent videoReturnedIntent) {
        File videoFile = getTempFile(context);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedVideo;
            boolean isCamera = (videoReturnedIntent == null ||
                    videoReturnedIntent.getData() == null);
            if (isCamera) {     /** CAMERA **/
                selectedVideo = Uri.fromFile(videoFile);
            } else {            /** ALBUM **/
                selectedVideo = videoReturnedIntent.getData();
            }
            return selectedVideo;
        }
        return null;
    }

    private static File getAlbumStorageDir(String albumName) {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), albumName);
    }

    public static String generatePath(Uri uri, Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            filePath = generateFromKitkat(uri, context);
        }

        if (filePath != null) {
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }

    @TargetApi(19)
    private static String generateFromKitkat(Uri uri, Context context) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Video.Media.DATA};
            String sel = MediaStore.Video.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);


            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }
}
