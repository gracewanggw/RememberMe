package com.example.rememberme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rememberme.DB.RememberMeDbSource;
import com.example.rememberme.quiz.Quiz;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddEditMemoryActivity extends AppCompatActivity implements View.OnClickListener/*, MultiSpinner.MultiSpinnerListener*/ {

    Context context;

    public static Memory memory;
    public static Framily framily;
    RememberMeDbSource dbSource;
    long framilyId;
    long memoryId;

    String fileName = "memory_img.jpg";
    String tempImgFileName = "profile.jpg";
    File pictureFile;
    File tempImgFile;
    Uri imageUri;

    public static final int CAMERA_REQUEST_CODE =  1;
    public static final int GALLERY_REQUEST_CODE = 2;

    public boolean audioPlay = false;
    private MediaRecorder recorder;
    private static final String LOG_TAG = "AudioRecording";
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 3;
    long tStart;

    EditText title;
    EditText text;
    ImageView image;
    TextView addImage;
    TextView audio;
    Button saveMemory;
    Button cancelMemory;
    TextView removeMemory;

//    MultiSpinner tagSpinner;
//    ArrayList<String> framNames;
//    ArrayList<String> selectedNames;
//    TextView tagged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        dbSource = new RememberMeDbSource(this);
        dbSource.open();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        fileName = "IMG_" + timeStamp + ".jpg";
        pictureFile = new File(getExternalFilesDir(null), fileName);
        imageUri = FileProvider.getUriForFile(this, "com.example.rememberme", pictureFile);
        tempImgFile = new File(getExternalFilesDir(null), tempImgFileName);

        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        image = findViewById(R.id.memory_image);
        addImage = findViewById(R.id.add_image);
        audio = findViewById(R.id.add_audio);
        //tagged = findViewById(R.id.tagged_names);
        saveMemory = findViewById(R.id.save_memory);
        cancelMemory = findViewById(R.id.cancel_memory);
        removeMemory = findViewById(R.id.remove);
        audio.setOnClickListener(this);
        saveMemory.setOnClickListener(this);
        cancelMemory.setOnClickListener(this);
        removeMemory.setOnClickListener(this);

//        tagSpinner = (MultiSpinner) findViewById(R.id.tag_spinner);
//        List<Framily> framilys = dbSource.fetchFramilyEntries();
//        selectedNames = new ArrayList<String>();
//        framNames = new ArrayList<String>();
//        for (Framily framily: framilys) {
//            framNames.add(framily.getNameFirst() + " " + framily.getNameLast());
//        }

        mFileName = getExternalFilesDir(null).getAbsolutePath() + "audio_file.3gp";

        Intent intent = getIntent();
        framilyId = intent.getLongExtra(FramilyProfile.ID_KEY, -1);
        Log.d("rdudak", "id = " + framilyId);
        framily = dbSource.fetchFramilyByIndex(framilyId);
        //tagged.setText("Tagged: " + framily.getNameFirst() + " " + framily.getNameLast());
        memoryId = intent.getLongExtra(ViewMemory.ID_MEMORY, -1);
        if (memoryId < 0) {
            Log.d("rdudak", "no id found -> new memory");
            memory = new Memory();
            removeMemory.setVisibility(View.INVISIBLE);
        }
        else {
            Log.d("rdudak", "memory id = " + memoryId);
            memory = dbSource.fetchMemoryByIndex(memoryId);
            loadData();
        }
//        framNames.remove(framily.getNameFirst() + " " + framily.getNameLast());
//        tagSpinner.setItems(framNames, "Select Framily Members to Tag", this);
        checkPermissions();

        context = this;

        audio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tStart = System.currentTimeMillis();
                        audio.setBackground(ContextCompat.getDrawable(context,R.drawable.mic_on));
                        startAudio();
                        break;
                    case MotionEvent.ACTION_UP:
                        audio.setBackground(ContextCompat.getDrawable(context,R.drawable.mic_off));
                        if((System.currentTimeMillis()-tStart)/1000 >=1){
                            saveAudio();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.add_audio:
//                if(!audioPlay) {
//                    startAudio();
//                }
//                else {
//                    saveAudio();
//                    audioPlay = false;
//                }
//                break;

            case R.id.save_memory:
                saveMemoryData();
                dbSource.close();
                finish();
                break;

            case R.id.cancel_memory:
                dbSource.close();
                finish();
                break;

            case R.id.remove:
                askRemove(this);
                break;
        }
    }

    public void saveMemoryData() {
        memory.setTitle(title.getText().toString());
        memory.setText(text.getText().toString());
        image.buildDrawingCache();
        Bitmap map = image.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(fileName,MODE_PRIVATE);
            map.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            memory.setFilename(fileName);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (memoryId >= 0) {
            dbSource.updateMemoryEntry(memoryId);
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
        }
        else {
            dbSource.insertMemory(memory);
            Toast.makeText(this, "New Memory Saved", Toast.LENGTH_SHORT).show();
            memoryId = dbSource.fetchLastMemoryEntry().getId();
            framily.addMemory(memoryId);
            dbSource.updateFramilyEntry(framilyId, framily);
            Log.d("rdudak", "updated framily id: " + framilyId + ", memories: " + framily.getMemories().toString());

        }
    }

    public void loadData() {
        title.setText(memory.getTitle());
        text.setText(memory.getText());
        if(memory.getImage() != null)
            updateImageView(memory.getImage());
    }

    public void askRemove(Context context) {
        final String field = "Are you sure you want to remove this memory?";
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(field)
                .setPositiveButton("Yes, I'm Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (framily.getMemories().size() == 1) {
                            FramilyProfile.removeLast = true;
                            framily.setMemories(new ArrayList<Long>());
                        }
                        else {
                            framily.removeMemory(memoryId);
                        }
                        dbSource.removeMemory(memoryId);
                        Log.d("rdudak", "Memories: " + framily.getMemories());
                       // Log.d("rdudak", "Memories: " + framily.getMemories());
                        dbSource.updateFramilyEntry(framilyId, framily);

//                        UpdateDbMemoryThread updateDbMemoryThread = new UpdateDbMemoryThread();
//                        updateDbMemoryThread.start();
//                        Intent intent = new Intent(context, FramilyProfile.class);
//                        intent.putExtra(FramilyProfile.ID_KEY, framilyId);
//                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No, go back!", null)
                .create();
        dialog.show();
    }

    private void checkPermissions()
    {
        if(Build.VERSION.SDK_INT < 23)
            return;
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 0);
        }
    }

    public void changeImage(View view) {
        String[] options = {"Open Camera", "Select from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Profile Picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Open Camera")) {
                    Log.d("rdudak", "camera selected");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
                else {
                    Log.d("rdudak", "gallery selected");
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , GALLERY_REQUEST_CODE);
                }
            }
        });
        builder.show();
        Log.d("rdudak", "The Change button has been clicked.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                try {
                    InputStream iStream = getContentResolver().openInputStream(imageUri);
                    byte[] image = getBytes(iStream);
                    memory.setImage(image);
                    updateImageView(image);
                    Log.d("rdudak", "camera bitmap saved: " + memory.getImage().toString());
                } catch (Exception e) {
                    Log.d("rdudak", "camera save failed");
                    e.printStackTrace();
                }
            }
            if (requestCode == GALLERY_REQUEST_CODE) {
                try {
                    imageUri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    bitmap.recycle();
                    memory.setImage(image);
                    updateImageView(image);
                    Log.d("rdudak", "gallery bitmap saved: " + memory.getImage().toString());
                } catch (Exception e) {
                    Log.d("rdudak", "gallery save failed");
                    e.printStackTrace();
                }
            }
            image.buildDrawingCache();
            Bitmap map = image.getDrawingCache();
            try {
                FileOutputStream fos = openFileOutput(tempImgFileName,MODE_PRIVATE);
                map.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            try {
                FileInputStream fis = openFileInput(tempImgFileName);
                Bitmap bmap = BitmapFactory.decodeStream(fis);
                image.setImageBitmap(bmap);
                fis.close();
            } catch (IOException e) {
                Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.memory);
                image.setImageBitmap(bm);
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void updateImageView(byte[] imageAr) {
        addImage.setVisibility(View.GONE);
        Bitmap bmp= BitmapFactory.decodeByteArray(imageAr, 0 , imageAr.length);
        Bitmap rotatedBmp = ImageRotation.rotateImage(bmp, 90);
        image.setImageBitmap(rotatedBmp);
    }

    public void startAudio() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(mFileName);
        try {
            recorder.prepare();
            recorder.start();
            audioPlay = true;
            Log.d("rdudak", "prepare() success");
        } catch (IOException e) {
            Log.d("rdudak", "prepare() failed");
        }
    }

    public void saveAudio() {
        recorder.stop();
        recorder.release();
        recorder = null;
        File audioFile = new File(getExternalFilesDir(null), mFileName);
        memory.setAudio(mFileName);
        Log.d("rdudak", "memory audio set");
        Toast.makeText(this, "Audio recording saved", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onItemsSelected(boolean[] selected) {
//        selectedNames.clear();
//        for (int i = 0; i < selected.length; i++) {
//            if (selected[i])
//                selectedNames.add(framNames.get(i));
//        }
//        String tag = "Tagged: " + framily.getNameFirst() + ", ";
//        for (String name: selectedNames) {
//            tag = tag + name + ", ";
//        }
//        tag = tag.substring(0, tag.length() -2);
//        tagged.setText(tag);
//    }

    public class UpdateDbMemoryThread extends Thread {
        @Override
        public void run() {
            dbSource.removeMemory(memoryId);
            Log.d("rdudak", "Memories: " + framily.getMemories());
            framily.removeMemory(memoryId);
            Log.d("rdudak", "Memories: " + framily.getMemories());
            dbSource.updateFramilyEntry(framilyId, framily);
            dbSource.close();
        }
    }
}