package braincollaboration.waper.views.dialog;


import android.content.Context;
import android.content.DialogInterface;

import braincollaboration.waper.R;

public class CheckInternetAccessDialog extends CustomDialogBase {

    public CheckInternetAccessDialog(Context context, DialogInterface.OnClickListener confirmCallback) {
        super(context, confirmCallback);
    }

    @Override
    protected int getTitle() {
        return R.string.notification_title;
    }

    @Override
    protected int getConfirmMessage() { return R.string.ok; }

    @Override
    protected int getCancelMessage() { //Is it necessary to deprecate not usable super variable?
        return -1;
    }

    @Override
    protected int getMessage() {
        return R.string.internet_connection_text;
    }

    @Override
    protected int getIconResId() {
        return R.drawable.ic_wan;
    }

}
