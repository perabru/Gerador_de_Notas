package com.bruno.geradordenotas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    //Criando variáveis globais

    Button btnGerar;
    Button btnMinhasNotas;
    Button btnHoje;

    TextView txtNome;
    TextView txtCPF;
    TextView txtData;
    TextView txtDescricao;
    TextView txtValor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Date currentTime = Calendar.getInstance().getTime();







        btnGerar = findViewById(R.id.btnGerar);
        btnMinhasNotas = findViewById(R.id.btnMinhasNotas);
        btnHoje = findViewById(R.id.btnHoje);
        txtNome = findViewById(R.id.txtNome);
        txtValor = findViewById(R.id.txtValor);
        txtData = findViewById(R.id.txtData);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtCPF = findViewById(R.id.txtCPF);



        btnGerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //criarPDF();
                salvarNota();
            }
        });

        btnHoje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                txtData.setText(currentDate);
            }
        });

        btnMinhasNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MinhasNotasActivity.class));
            }
        });
    }




     public void salvarNota(){

        try{
            SQLiteDatabase geradorNotasDB = this.openOrCreateDatabase("Notas", MODE_PRIVATE, null);

            geradorNotasDB.execSQL("CREATE TABLE IF NOT EXISTS tblMinhasNotas(notasID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nome VARCHAR, CPF VARCHAR,valor DOUBLE, data VARCHAR, descricao VARCHAR)");

            //geradorNotasDB.execSQL("INSERT INTO tblMinhasNotas (nome, CPF, valor, data, descricao) VALUES(mNome, )");

            ContentValues contentValues = new ContentValues();

            contentValues.put("nome", txtNome.getText().toString());
            contentValues.put("CPF", txtCPF.getText().toString());
            contentValues.put("valor", txtValor.getText().toString());
            contentValues.put("data", txtData.getText().toString());
            contentValues.put("descricao", txtDescricao.getText().toString());
            geradorNotasDB.insert("tblMinhasNotas", null, contentValues);

            Context contexto = getApplicationContext();
            String texto = "SALVO COM SUCESSO";
            int duracao = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(contexto, texto,duracao);
            toast.show();


        }catch(Exception ex){

            Context contexto = getApplicationContext();
            String texto = "Erro: "+ex.toString();
            int duracao = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(contexto, texto,duracao);
            toast.show();


        }

     }
    private void criarPDF() {
        // TODO Auto-generated method stub
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();



        try {
           String path = Environment.getExternalStorageDirectory().getAbsolutePath() ;
            //String path = "/storage/GeradorDeNotas";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);


            File file = new File(dir, "nota.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(document, fOut);

            //open the document
            document.open();


            Paragraph p1 = new Paragraph("Sample PDF CREATION USING IText");
            Font paraFont= new Font(Font.FontFamily.COURIER);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            document.add(p1);

            Paragraph p2 = new Paragraph("This is an example of a simple paragraph");
            Font paraFont2= new Font(Font.FontFamily.COURIER,14.0f,0, CMYKColor.GREEN);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(paraFont2);

            document.add(p2);


            Context contexto = getApplicationContext();
            String texto = "PDF CRIADO COM SUCESSO: "+path;
            int duracao = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(contexto, texto,duracao);
            toast.show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri mydir = Uri.parse("/storage/emulated/0/nota.pdf");
            intent.setDataAndType(mydir, "*/*");
            startActivity(intent);



        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally
        {
            document.close();
        }

    }

}
