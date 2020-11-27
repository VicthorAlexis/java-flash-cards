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
    ArrayList<Card> cards;

    public Deck(String nome) {
        this.nome = nome;
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
    
}
