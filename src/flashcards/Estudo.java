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
    private Card primeiroCard;  // card atual
    private int[] ordemCards;   // TODO controlar a ordem dos cards no deck
    private Iterator<Card> it;  // para mostrar o próximo card na fila
    
    private Deck deck;
    // numero de acertos no estudo (para a pontuação)
    private int numAcertos = 0;
    
    
    public Estudo(Deck deck) {
        this.deck = deck;
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
    
    // incrementa o número de acertos do card à mostra e do estudo
    public void acertei() {
        numAcertos += 1;
        primeiroCard.setNumAcertos(primeiroCard.getNumAcertos() + 1);
    }
    
    // incrementa o número de erros do card à mostra e do estuo
    public void errei() {
        primeiroCard.setNumErros(primeiroCard.getNumErros() + 1);
    }

    public int getNumAcertos() {
        return numAcertos;
    }
    
    public Estudo novoEstudo() {
        return new Estudo(deck);
    }
}
