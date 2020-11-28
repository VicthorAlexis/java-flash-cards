/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashcards;

import java.util.ArrayList;

/**
 *
 * @author Victhor-pc
 */
public class User {
    
    private String nome;
    private String senha;
    private Data data;
    ArrayList<Deck> decks;

    public User(String nome, String senha, Data data) {
        this.nome = nome;
        this.senha = senha;
        this.data = data;
        decks = new ArrayList();
    }
    
    public void modificar(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }  
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    
}
