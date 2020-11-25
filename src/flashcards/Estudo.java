/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashcards;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author felip
 */
public class Estudo {
    private final Queue<Card> filaCards; // fila de cards do deck 
    private Card primeiroCard;
    private int[] ordemCards;   // controla a ordem dos cards no deck
    Iterator<Card> it;
    
    
    public Estudo(Deck deck) {
        filaCards = new LinkedList<>(deck.getCards());
        it = filaCards.iterator();
    }
    
    // retorna o próximo card do deck
    public Card proximoCard() {
        if (it.hasNext()) {
            primeiroCard = it.next();
            return primeiroCard;
        }
        return null;
    }
    
    public Card getPrimeiroCard() {
        return primeiroCard;
    }
    
    // incrementa o número de acertos do card à mostra
    public void acertei() {
        primeiroCard.setNumAcertos(primeiroCard.getNumAcertos() + 1);
    }
    
    // incrementa o número de erros do card à mostra
    public void errei() {
        primeiroCard.setNumErros(primeiroCard.getNumErros() + 1);
    }
    
    

}
