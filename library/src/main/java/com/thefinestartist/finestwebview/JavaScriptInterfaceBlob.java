package com.thefinestartist.finestwebview;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class JavaScriptInterfaceBlob {
    private Context context;
    private NotificationManager nm;
    public JavaScriptInterfaceBlob(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void getBase64FromBlobData(String base64Data, String fileName) throws IOException {
        convertBase64StringToPdfAndStoreIt(base64Data, fileName);
    }
   
    private void convertBase64StringToPdfAndStoreIt(String base64PDf, String fileName) throws IOException {
        final int notificationId = 1;
        String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        final File dwldsPath = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/" + fileName);
        byte[] pdfAsBytes = Base64.decode(base64PDf.replaceFirst("^data:application/pdf;base64,", ""), 0);
        FileOutputStream os;
        os = new FileOutputStream(dwldsPath, false);
        os.write(pdfAsBytes);
        os.flush();

        if(dwldsPath.exists()) {
            NotificationCompat.Builder b = new NotificationCompat.Builder(context, "MY_DL")
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("Smart'POS")
                    .setContentText("Téléchargement..");
            nm = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
            if(nm != null) {
                nm.notify(notificationId, b.build());
                Handler h = new Handler();
                long delayInMilliseconds = 5000;
                h.postDelayed(new Runnable() {
                    public void run() {
                        nm.cancel(notificationId);
                    }
                }, delayInMilliseconds);
            }
        }
    }
}
