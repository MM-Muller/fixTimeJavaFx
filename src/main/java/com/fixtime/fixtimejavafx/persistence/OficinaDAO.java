package com.fixtime.fixtimejavafx.persistence; // define o pacote para organizar as classes

import com.fixtime.fixtimejavafx.model.Oficina; // importa a classe Oficina da model
import java.io.*; //  possui classes para entrada e saída de dados, como leitura e escrita de arquivos.
import java.util.ArrayList;

public class OficinaDAO {
    private static final String FILE_NAME = System.getProperty("user.dir") + "/data/oficinas.dat"; // user.dir retorna o caminho da pasta onde o seu projeto está sendo executado no momento
    // com static não precisa criar um objeto OficinaDAO para acessar FILE_NAME
    // o final indica que o valor de FILE_NAME não pode ser alterado (constante)

    public static void salvar(ArrayList<Oficina> oficinas) throws IOException { // metodo recebe uma lista de Oficinas e salva no .dat
        // void não retorna nenhum valor
        // throws IOException -> pode lançar uma exceção de entrada/saída, ou seja quem chama o metodo DEVE tratar usando try-catch
        File pasta = new File(FILE_NAME).getParentFile(); // getParentFile() retorna um novo objeto File representando uma pasta antes (diretório pai)
        if (!pasta.exists()) pasta.mkdirs(); // caso não exista, cria a pasta data

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
        // FileOutputStream: Abre o arquivo no disco para escrita de bytes
        // ObjectOutputStream: Converte objetos Java em bytes (serializa) e os envia para o FileOutputStream.

        oos.writeObject(oficinas);// transfomra o ArrayList em uma sequência de bytes.
        oos.close();// fecha o fluxo de saída
    }

    public static ArrayList<Oficina> carregar() throws IOException, ClassNotFoundException {
        // IOException: erro na entrada e saída de dados (Input/Output - I/O)
        // ClassNotFoundException: Acha o objeto mas não acha a Classe
        File file = new File(FILE_NAME); // representa o caminho e o nome do arquivo
        if (!file.exists()) return new ArrayList<>(); // vai verificar se o arquivo existe

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
        // FileInputStream: conecta o programa com o Arquivo físico
        // ObjectInputStream: transforma bytes armazenados em objetos Java.

        ArrayList<Oficina> lista = (ArrayList<Oficina>) ois.readObject(); // pega oque foi enviado com writeObject e remonta em Objeto
        ois.close(); // fecha o arquivo  e retorna a lista
        return lista;
    }
}