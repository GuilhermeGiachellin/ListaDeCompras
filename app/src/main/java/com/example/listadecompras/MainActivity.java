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

        }
    }

    public void inserirProdutos() {

    }
}