/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashcards;

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
    
    public ArrayList<Card> getCards() {
        return this.cards;
    }
    
    public String getNome() {
        return this.nome;
    }

    public Data getData() {
        return data;
    }
    
}
