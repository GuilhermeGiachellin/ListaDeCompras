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

import java.util.ArrayList;
import java.util.Calendar;

public class ListaCompras extends AppCompatActivity {

    private SQLiteDatabase bancoDados;
    private ListView listViewProdutos;
    private EditText editTextNomeLista;
    private String produtoSelecionado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compras);

        listViewProdutos = (ListView) this.findViewById(R.id.listViewProdutos);
        editTextNomeLista = (EditText) this.findViewById(R.id.editTextNomeLista);

        listarProdutos();

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                produtoSelecionado = (String) (listViewProdutos.getItemAtPosition(position));
            }
        });
    }

    public void adcionarNovaLista() {
        if(editTextNomeLista.getText().toString().trim().equals("")) {
            Uteis.Alert(this, "Nome da lista é obrigatório");
            editTextNomeLista.requestFocus();

        } else if(produtoSelecionado.isEmpty()) {
            Uteis.Alert(this, "Selecione um produto");

        } else {
            try {
                bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);
                long produtoId, listaId;

                String sql = "INSERT INTO lista (nome, data) VALUES(?, ?)";
                SQLiteStatement stmt = bancoDados.compileStatement(sql);

                stmt.bindString(1, editTextNomeLista.getText().toString());
                stmt.bindString(2, String.valueOf(Calendar.getInstance().getTime()));
                //EXECUTA INSERT E PEGA A ID DA NOVA LISTA
                listaId = stmt.executeInsert();

                sql = "SELECT id FROM produto WHERE nome = ?";
                stmt = bancoDados.compileStatement(sql);
                stmt.bindString(1, produtoSelecionado);
                //PEGA A ID DO PRODUTO SELECIONADO
                produtoId = stmt.executeInsert();

                //INSERE NA TABELA DE LIGACAO
                sql = "INSERT INTO listaComprasProdutos (produto_id, lista_id) VALUES(?, ?)";
                stmt = bancoDados.compileStatement(sql);

                stmt.bindLong(1, produtoId);
                stmt.bindLong(2, listaId);
                stmt.executeInsert();

                bancoDados.close();

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
            e.printStackTrace();
        }
    }

    public void salvarListaButton(View v) {
        adcionarNovaLista();
        startActivity(new Intent(this, MainActivity.class));

    }

}