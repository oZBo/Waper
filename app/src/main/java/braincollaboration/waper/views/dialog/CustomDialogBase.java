package braincollaboration.waper.views.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public abstract class CustomDialogBase {

    protected abstract int getTitle();
    protected abstract int getConfirmMessage();
    protected abstract int getCancelMessage();
    protected abstract int getMessage();

    protected abstract int getIconResId();

    private AlertDialog.Builder dialogBuilder;
    private Dialog dialog;

    public CustomDialogBase(Context context) {
        this(context, null);
    }

    public CustomDialogBase(Context context, DialogInterface.OnClickListener confirmClickListener){
        this(context, confirmClickListener, null); // Why this method ain't starts with initDialog as 30th line? What's the difference?
    }

    public CustomDialogBase(Context context, DialogInterface.OnClickListener confirmClickListener, DialogInterface.OnClickListener cancelClickListener){
        initDialog(context, confirmClickListener, cancelClickListener);
    }

    private AlertDialog.Builder initDialog(Context context, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener) {
        dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(context.getString(getTitle()));
        dialogBuilder.setMessage(context.getString(getMessage()));
        dialogBuilder.setIcon(getIconResId()); //Why this (and at 38th, 42ht lines) argument ain't start with context use as it made at 35th, 36th lines?
        if(confirmListener != null){
            dialogBuilder.setPositiveButton(getConfirmMessage(), confirmListener);
        }
        if(cancelListener != null){
            dialogBuilder.setNegativeButton(getCancelMessage(), cancelListener);
        }
        return dialogBuilder;
    }

    public void showDialog(){
        if(dialogBuilder != null){
            dialog = dialogBuilder.create();
            dialog.show();
        }
    }

    public void dismiss(){
        if(dialog != null){
            dialog.dismiss();
        }
    }

}
