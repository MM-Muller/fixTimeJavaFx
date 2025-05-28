package com.fixtime.fixtimejavafx.model;

import java.io.Serializable;

public class Veiculo implements Serializable {
    private int id;
    private String tipo;
    private String marca;
    private String modelo;
    private int ano;
    private String cor;
    private String placa;
    private double quilometragem;

    public Veiculo(int id, String tipo, String marca, String modelo, int ano, String cor, String placa, double quilometragem) {
        this.id = id;
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.cor = cor;
        this.placa = placa;
        this.quilometragem = quilometragem;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public double getQuilometragem() { return quilometragem; }
    public void setQuilometragem(double quilometragem) { this.quilometragem = quilometragem; }

    @Override
    public String toString() {
        return tipo + " - " + modelo + " - " + placa;
    }
}