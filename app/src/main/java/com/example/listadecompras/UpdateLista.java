package com.example.listadecompras;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class UpdateLista extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private ListView listViewProdutos;
    private long listaId, produtoId;
    private ArrayList<Long> arrayIds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_lista);

        listViewProdutos = (ListView) this.findViewById(R.id.listViewProdutos);

        Bundle bundle = getIntent().getExtras();
        listaId = bundle.getInt("listaId");
        produtoId = bundle.getLong("produtoId");

        listarProdutos();

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                updateProduto(position);
            }
        });
    }

    public void listarProdutos() {
        try {
            bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);

            //PEGA PRODUTOS RELACIONADOS A LISTA
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
                arrayIds.add(meuCursor.getLong(0));
                linhasDados.add(meuCursor.getString(1));
                meuCursor.moveToNext();
            }

            bancoDados.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProduto(int position) {
        try {
            bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);

            String sql = "UPDATE listaComprasProdutos SET produto_id = ? WHERE lista_id = ? AND produto_id = ?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);

            stmt.bindLong(1, arrayIds.get(position));
            stmt.bindLong(2, listaId);
            stmt.bindLong(3, produtoId);
            stmt.executeUpdateDelete();
            
            bancoDados.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        finish();
    }
}