package braincollaboration.waper.views.dialog;


import android.content.Context;
import android.content.DialogInterface;

import braincollaboration.waper.R;

public class SetAsWallpaperDialog extends CustomDialogBase {


    public SetAsWallpaperDialog(Context context, DialogInterface.OnClickListener confirmCallback, DialogInterface.OnClickListener cancelCallback) {
        super(context, confirmCallback, cancelCallback);
    }

    @Override
    protected int getTitle() {
        return R.string.confirm_title;
    }

    @Override
    protected int getConfirmMessage() {
        return R.string.yes;
    }

    @Override
    protected int getCancelMessage() {
        return R.string.cancel;
    }

    @Override
    protected int getMessage() {
        return R.string.set_as_wallpaper_question;
    }

    @Override
    protected int getIconResId() {
        return R.drawable.ic_question_answer_white_48dp;
    }

}
