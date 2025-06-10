package com.fixtime.fixtimejavafx.persistence;

import com.fixtime.fixtimejavafx.model.Cliente;

import java.io.*;
import java.util.ArrayList;

public class ClienteDAO{
    private static final String FILE_NAME = System.getProperty("user.dir") + "/data/clientes.dat";

    public static void salvar(ArrayList<Cliente> clientes) throws IOException {
        File pasta = new File(FILE_NAME).getParentFile();
        if (!pasta.exists()) pasta.mkdirs();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
        oos.writeObject(clientes);
        oos.close();
    }

    public static ArrayList<Cliente> carregar() throws IOException, ClassNotFoundException {
        // lê os bytes do arquivo e então transforma em objetos Cliente
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
        ArrayList<Cliente> lista = (ArrayList<Cliente>) ois.readObject();
        ois.close();
        return lista;
    }
}