package com.fixtime.fixtimejavafx.persistence;

import com.fixtime.fixtimejavafx.model.Oficina;

import java.io.*;
import java.util.ArrayList;

public class OficinaDAO {
    private static final String FILE_NAME = System.getProperty("user.dir") + "/data/oficinas.dat";

    public static void salvar(ArrayList<Oficina> oficinas) throws IOException {
        File pasta = new File(FILE_NAME).getParentFile();
        if (!pasta.exists()) pasta.mkdirs();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
        oos.writeObject(oficinas);
        oos.close();
    }

    public static ArrayList<Oficina> carregar() throws IOException, ClassNotFoundException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
        ArrayList<Oficina> lista = (ArrayList<Oficina>) ois.readObject();
        ois.close();
        return lista;
    }
}