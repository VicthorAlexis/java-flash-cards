/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashcards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author felip
 */
public class Estudo {
    private Queue<Card> filaCards; // fila de cards do deck
    // fila para o próximo estudo (com a nova ordem de cards definida pelos acertos)
    private Queue<Card> proximaFila;
    private Card primeiroCard;  // card atual
    private Iterator<Card> iterator;  // iterator para mostrar o próximo card na fila
    private int numAcertos = 0; // numero de acertos no estudo (para a pontuação)
    
    private Deck deck;

    public Estudo(Deck deck) {
        this.deck = deck;
        filaCards = new LinkedList<>(deck.getCards());
        proximaFila = new LinkedList<>(deck.getCards());
        iterator = filaCards.iterator();
    }
    
    // retorna o próximo card do deck
    public Card proximoCard() {
        if (iterator.hasNext()) {
            primeiroCard = iterator.next();
            return primeiroCard;
        }
        return null;
    }
    
    // retorna o card à mostra
    public Card getPrimeiroCard() {
        return primeiroCard;
    }
    
    // incrementa o número de acertos do card à mostra e do estudo
    public void acertei() {
        numAcertos += 1;
        primeiroCard.setNumAcertos(primeiroCard.getNumAcertos() + 1);
        
        // se acertou, realocar para o fim da fila de cards
        Card cardAcertou = proximaFila.remove();
        proximaFila.add(cardAcertou);
    }
    
    // retorna a lista atualizada de cards
    public ArrayList<Card> novaOrdemCards() {
        deck.setCards(new ArrayList<>(proximaFila));
        return deck.getCards();
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
