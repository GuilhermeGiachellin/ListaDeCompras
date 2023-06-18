package com.example.listadecompras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class ListaCompras extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private TextView textViewNome;
    private ListView listViewProdutos;

    private String nomeLista;
    private String produtoSelecionado;
    private long produtoId, listaId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compras);

        listViewProdutos = (ListView) this.findViewById(R.id.listViewProdutos);
        textViewNome = (TextView) this.findViewById(R.id.textViewNome);

        Bundle bundle = getIntent().getExtras();
        nomeLista = bundle.getString("nomeLista");
        listaId = bundle.getInt("listaId");
        textViewNome.setText(nomeLista);

        listarProdutos();
        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                produtoSelecionado = (String) (listViewProdutos.getItemAtPosition(position));
            }
        });
    }

    public void listarProdutos() {
            try {
                bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);
//                Cursor meuCursor = bancoDados.rawQuery("SELECT id FROM listaComprasProdutos WHERE lista_id = '"+listaId+"'", null);
//                meuCursor.moveToFirst();
//                Log.i("KRAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", " " + meuCursor.getString(0));
                //PEGA PRODUTOS RELACIONADOS A LISTA
                Cursor meuCursor = bancoDados.rawQuery("SELECT produto.nome FROM lista INNER JOIN listaComprasProdutos ON lista_id = lista.id INNER JOIN produto ON produto_id = produto.id WHERE lista.id = '"+listaId+"'", null);
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
                    linhasDados.add(meuCursor.getString(0));
                    meuCursor.moveToNext();
                }

                bancoDados.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public  void voltarButton(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void deletarListaButton(View v) {
        try {
            bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);

            String sql = "DELETE FROM lista WHERE id = ?";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, listaId);
            stmt.executeUpdateDelete();

            sql = "DELETE FROM listaComprasProdutos WHERE lista_id = ?";
            stmt = bancoDados.compileStatement(sql);
            stmt.bindLong(1, listaId);
            stmt.executeUpdateDelete();

            bancoDados.close();

            Uteis.Alert(this,"LISTA EXCLUIDA");
            startActivity(new Intent(this, MainActivity.class));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void listarProdutos() {
//        try {
//            bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);
//            Cursor meuCursor = bancoDados.rawQuery("SELECT id, nome FROM produto", null);
//
//
//            ArrayList<String> linhasDados = new ArrayList<String>();
//
//            ArrayAdapter adapterDados = new ArrayAdapter<String>(
//                    this,
//                    android.R.layout.simple_list_item_1,
//                    android.R.id.text1,
//                    linhasDados
//            );
//
//            listViewProdutos.setAdapter(adapterDados);
//            meuCursor.moveToFirst();
//            while(meuCursor != null) {
//                linhasDados.add(meuCursor.getString(1));
//                meuCursor.moveToNext();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void salvarListaButton(View v) {
//        adcionarNovaLista();
//        Uteis.Alert(this,"LISTA SALVA");
//        limparCampos();
//        startActivity(new Intent(this, MainActivity.class));
//
//    }
//

}