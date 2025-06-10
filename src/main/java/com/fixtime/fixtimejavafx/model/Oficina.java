package com.fixtime.fixtimejavafx.model;

import java.io.Serializable;

public class Oficina implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private String categoria;
    private String cnpj;
    private String telefone;
    private String email;
    private String cep;
    private String endereco;
    private String senha;

    public Oficina(int id, String nome, String categoria, String cnpj, String telefone, String email, String cep, String endereco) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.email = email;
        this.cep = cep;
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCnpj() {
        return cnpj;
    }
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}