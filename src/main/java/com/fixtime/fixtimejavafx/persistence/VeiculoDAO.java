package com.fixtime.fixtimejavafx.persistence;

import com.fixtime.fixtimejavafx.model.Veiculo;

import java.io.*;
import java.util.ArrayList;

public class VeiculoDAO {
    private static final String FILE_NAME = System.getProperty("user.dir") + "/data/veiculos.dat";

    public static void salvar(ArrayList<Veiculo> veiculos) throws IOException {
        File pasta = new File(FILE_NAME).getParentFile();
        if (!pasta.exists()) pasta.mkdirs();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
        oos.writeObject(veiculos);
        oos.close();
    }

    public static ArrayList<Veiculo> carregar() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
        ArrayList<Veiculo> lista = (ArrayList<Veiculo>) ois.readObject();
        ois.close();
        return lista;
    }
}
