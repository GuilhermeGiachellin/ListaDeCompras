package com.example.listadecompras;

import android.app.AlertDialog;
import android.content.Context;

public class Uteis {
    public static void Alert(Context context, String mensagem){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.app_name);
        alertDialog.setMessage(mensagem);
        alertDialog.setPositiveButton("OK", null);
        alertDialog.show();
    }
}
