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
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    private ListView listViewListas;
    private ArrayList<Integer> arrayIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewListas = (ListView) findViewById(R.id.listViewListas);

        criarBancoDados();
        //inserirProdutos();
        listarListas();

        listViewListas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String nomeLista = (String) (listViewListas.getItemAtPosition(position));
                //ENVIA O NOME DA LISTA SELECIONADA NO ITENT
                irParaLista(nomeLista, position);
            }
        });
    }
    public void criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);
            //TABELA DE PRODUTO
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS produto(" + " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                               " , nome VARCHAR" +
                               " , valor DOUBLE)");
            //TABELA DE LISTA
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS lista(" + " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR" +
                    " , data DATE)");
            //TABELA DE CONEXAO
            bancoDados.execSQL("CREATE TABLE listaComprasProdutos(" +
                    "   ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , produto_id INTEGER " +
                    " , lista_id INTEGER" +
                    " , FOREIGN KEY (produto_id) REFERENCES produto(id)" +
                    " , FOREIGN KEY (lista_id) REFERENCES lista(id)" +
                    ");");
            bancoDados.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inserirProdutos() {
        try {
            bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);

            String sql = "INSERT INTO produto (nome, valor) VALUES(?, ?)";
            SQLiteStatement stmt = bancoDados.compileStatement(sql);

            stmt.bindString(1, "Arroz 1 Kg");
            stmt.bindDouble(2, 2.69);
            stmt.executeInsert();

            stmt.bindString(1, "Leite curta vida");
            stmt.bindDouble(2, 2.70);
            stmt.executeInsert();

            stmt.bindString(1, "Carne Freeboi");
            stmt.bindDouble(2, 16.70);
            stmt.executeInsert();

            stmt.bindString(1, "Feiajo carioquinha");
            stmt.bindDouble(2, 3.38);
            stmt.executeInsert();

            stmt.bindString(1, "Coca-Cola");
            stmt.bindDouble(2, 3.00);
            stmt.executeInsert();

            bancoDados.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarListas() {
        try {
            arrayIds = new ArrayList<>();
            bancoDados = openOrCreateDatabase("listaDeComprasDb", MODE_PRIVATE, null);
            Cursor meuCursor = bancoDados.rawQuery("SELECT id, nome FROM lista", null);
            ArrayList<String> linhasDados = new ArrayList<String>();

            ArrayAdapter adapterDados = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhasDados
            );

            //if(meuCursor != null) {
                listViewListas.setAdapter(adapterDados);
                meuCursor.moveToFirst();
                while(meuCursor != null) {
                    linhasDados.add(meuCursor.getString(1));
                    arrayIds.add(meuCursor.getInt(0));
                    meuCursor.moveToNext();
                }

            //} else {
//                linhasDados.add("Não a listas criadas");

            //}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void irParaLista(String nomeLista, int posicao) {
        Intent intent = new Intent(this, ListaCompras.class);
        intent.putExtra("nomeLista", nomeLista);
        intent.putExtra("listaId", arrayIds.get(posicao));
        startActivity(intent);
    }
    public void novaListaButton(View v) {
        startActivity(new Intent(this, CadastrarLista.class));
    }

}