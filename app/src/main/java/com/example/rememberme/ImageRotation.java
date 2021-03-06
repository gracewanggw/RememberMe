package com.example.rememberme;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

public class ImageRotation {

//    public static Bitmap checkRotation (Bitmap bitmap) throws IOException {
//        ExifInterface ei = new ExifInterface(path);
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                ExifInterface.ORIENTATION_UNDEFINED);
//
//        Bitmap rotatedBitmap = null;
//        switch (orientation) {
//
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                rotatedBitmap = rotateImage(bitmap, 90);
//                break;
//
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                rotatedBitmap = rotateImage(bitmap, 180);
//                break;
//
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                rotatedBitmap = rotateImage(bitmap, 270);
//                break;
//
//            case ExifInterface.ORIENTATION_NORMAL:
//            default:
//                rotatedBitmap = bitmap;
//        }
//
//        return rotatedBitmap;
//
//    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
