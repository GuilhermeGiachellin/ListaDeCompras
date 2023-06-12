package com.example.listadecompras;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        criarBancoDados();
    }

    public void criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS produto(" + " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                               " , nome VARCHAR" +
                               " , valor DOUBLE)");
            bancoDados.close();
        } catch (Exception e) {

        }
    }
}