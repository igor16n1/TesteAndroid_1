package com.testetiquei.igor.testetiquei;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;

public class CameraActivity extends AppCompatActivity {
    private String email;
    private String name;
    private String gender;
    private TextView usuarioData;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Button btnFoto = (Button) findViewById(R.id.btnFoto);
        Button btnEnviar = (Button) findViewById(R.id.btnEnviar);
        Intent myIntent = getIntent();
        email = myIntent.getStringExtra("email");
        name = myIntent.getStringExtra("name");
        gender = myIntent.getStringExtra("gender");
        usuarioData = (TextView) findViewById(R.id.usuarioData);
        usuarioData.setText(email + " " + name + " " + gender);
        img = (ImageView)findViewById(R.id.imagem);

        File picture = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/temp.jpg");
        Uri imgUri=Uri.fromFile(picture);
        img.setImageURI(imgUri);

        btnFoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "temp.jpg")));
                startActivityForResult(intent, 2);

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String[] TO = {email};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, name + " email app");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Nome: " + name + " \nSexo: " + gender );
                String path =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/temp.jpg";
                Uri uri = Uri.parse("file://" + path);
                emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {

                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == 0)
            return;
        if (requestCode == 2) {
            File picture = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/temp.jpg");
            Uri imgUri=Uri.fromFile(picture);
            ImageView image = (ImageView)findViewById(R.id.imagem);
            image.setImageResource(0);
            image.setImageURI(imgUri);
        }
    }

    public void Voltar(final View view)
    {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
