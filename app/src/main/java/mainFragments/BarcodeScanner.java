package mainFragments;

        import androidx.annotation.NonNull;
        import androidx.annotation.RequiresApi;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;

        import android.Manifest;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Vibrator;
        import android.util.Log;
        import android.util.SparseArray;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.librarypass.MainActivity;
        import com.example.librarypass.R;
        import com.google.android.gms.vision.CameraSource;
        import com.google.android.gms.vision.Detector;
        import com.google.android.gms.vision.barcode.Barcode;
        import com.google.android.gms.vision.barcode.BarcodeDetector;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.FirebaseFirestore;

        import java.io.IOException;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;

public class BarcodeScanner extends AppCompatActivity {
    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    FirebaseFirestore fstore;
    BarcodeDetector barcodeDetector;
    public static  final String Shared_pref="sharedPrefs";
    static int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
        textView = findViewById(R.id.textview);
        fstore = FirebaseFirestore.getInstance();

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).
                setRequestedPreviewSize(640, 480).build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
                    return;

                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();

            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCode = detections.getDetectedItems();
                if (qrCode.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            String currentuserid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Calendar calfordate = Calendar.getInstance();
                            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                            String day = dayFormat.format(calfordate.getTime());
                            String uid=day.concat(currentuserid);
                            String value = getIntent().getExtras().getString("task");
                            String hostel=getIntent().getExtras().getString("hostel");
                            if(value.equals("verification")) {
                                Calendar calfortime = Calendar.getInstance();
                                SimpleDateFormat currenttimeformat = new SimpleDateFormat("hh:mm a");
                                String currenttime = currenttimeformat.format(calfortime.getTime());
                                if (currenttime.equals(qrCode.valueAt(0).displayValue)) {
                                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(500);
                                    fstore.collection("Library")
                                            .document(uid)
                                            .update("status", "1");
                                    fstore.collection("Hostel").document(hostel).collection("studentList").document(uid)
                                            .update("libStatus","1");
                                    Intent intent = new Intent(BarcodeScanner.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                                else
                                {
                                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(1000);
                                    textView.setText("Scanning error!! Please Scan again!!");
                                }
                            }
                            else if(value.equals("return")) {
                                Calendar calfortime = Calendar.getInstance();
                                SimpleDateFormat currenttimeformat = new SimpleDateFormat("hh:mm a");
                                String currenttime = currenttimeformat.format(calfortime.getTime());
                                if (currenttime.equals(qrCode.valueAt(0).displayValue)) {
                                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(500);
                                    fstore.collection("Hostel").document(hostel).collection("studentList").document(uid)
                                            .update("status","1");
                                    SharedPreferences sharedPreferences = getSharedPreferences("hostel_return", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("return", "unread");
                                    editor.apply();
                                    Intent intent = new Intent(BarcodeScanner.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                                else
                                {
                                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(1000);
                                    textView.setText("Scanning error!! Please Scan again!!");
                                }
                            }
                        }
                    });
                }

            }
        });


    }
}