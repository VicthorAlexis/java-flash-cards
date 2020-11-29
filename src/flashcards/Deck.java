/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashcards;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author felip
 */
public class Deck {
    private String nome;
    private Data data;
    
    // Referências às outras classes
    User user;
    ArrayList<Card> cards;

    public Deck(String nome, Data data) {
        this.nome = nome;
        this.data = data;
        cards = new ArrayList<>();
    }
    
    public void addCard(Card c) {
        cards.add(c);
    }
    
    public void salvarDeck(Deck deck) {
        try{
            File file = new File("deck.txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter pr = new PrintWriter(br);
            
            pr.println(deck.getUser() + ";" + deck.getNome() + ";" + deck.getData());
            pr.close();
            br.close();
            fr.close();
            
        }catch(Exception e) {
            System.out.println("Erro ao escrever no arquivo!");
        }
    }
    
    public ArrayList<Card> getCards() {
        return this.cards;
    }
    
    public String getNome() {
        return this.nome;
    }

    public Data getData() {
        return data;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
}
