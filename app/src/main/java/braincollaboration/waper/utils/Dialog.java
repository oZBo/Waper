package braincollaboration.waper.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import braincollaboration.waper.R;

public class Dialog {

    private Context context;
    private Bitmap mainBitmap;

    public Dialog(Context context, Bitmap mainBitmap) {
        this.context = context;
        this.mainBitmap = mainBitmap;
    }

    public Dialog(Context context) {
        this.context = context;
    }

    public android.app.Dialog showDialog (int id) {
        if (id == Constants.DIALOG_INTERNET_NOT_AVIABLE) { //internet check dialog
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle(R.string.notification_title);
            adb.setMessage(R.string.internet_connection_text);
            adb.setIcon(R.drawable.ic_wan);
            adb.setNeutralButton(R.string.ok, myClickListener);
            return adb.create();
        } else if (id == Constants.DIALOG_SET_WALLPAPER_QUESTION) { //Set wallpaper dialog confirm
            AlertDialog.Builder adb = new AlertDialog.Builder(context);
            adb.setTitle(R.string.confirm_title);
            adb.setMessage(R.string.set_as_wallpaper_question);
            adb.setIcon(R.drawable.ic_question_answer_white_48dp);
            adb.setPositiveButton(R.string.yes, myClickListener);
            adb.setNegativeButton(R.string.cancel, myClickListener);
            return adb.create();
        }
        return null;
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case android.app.Dialog.BUTTON_POSITIVE:
                    setAsWallpaper();
                    break;
                case android.app.Dialog.BUTTON_NEGATIVE:
                    break;
                case android.app.Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };

    private void setAsWallpaper() {
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            wallpaperManager.setBitmap(mainBitmap);
            Toast.makeText(context, R.string.wallpaper_set, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
