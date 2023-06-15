package com.example.listadecompras;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class ListaCompras extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private ListView listViewProdutos;
    private EditText editTextNomeLista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compras);

        listViewProdutos = (ListView) this.findViewById(R.id.listViewProdutos);
        editTextNomeLista = (EditText) this.findViewById(R.id.editTextNomeLista);

        listarProdutos();
    }

    public void adcionarNovaLista() {
        if(editTextNomeLista.getText().toString().trim().equals("")) {
            Uteis.Alert(this, "Nome da lista é obrigatório");
            editTextNomeLista.requestFocus();
        } else {
            try {
                bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);

                String sql = "INSERT INTO lista (nome, data) VALUES(?, ?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);

                stmt.bindString(1, editTextNomeLista.getText().toString());
                stmt.bindString(2, String.valueOf(Calendar.getInstance().getTime()));
                stmt.executeInsert();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listarProdutos() {
        try {
            bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, nome FROM produto", null);
            ArrayList<String> linhasDados = new ArrayList<String>();

            ArrayAdapter adapterDados = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhasDados
            );

            listViewProdutos.setAdapter(adapterDados);
            meuCursor.moveToFirst();
            while(meuCursor != null) {
                linhasDados.add(meuCursor.getString(1));
                meuCursor.moveToNext();
            }

        } catch (Exception e) {

        }
    }

}