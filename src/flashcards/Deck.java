/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashcards;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author felip
 */
public class Deck {
    private String nome;
    private Data data;
    private boolean carregouDados = false;
    private int VezesEstudadas = 0;
    private int indiceDoDeckAtual;
    
    // Referências às outras classes
    private User user;
    ArrayList<Card> cards;

    public Deck(String nome, Data data) {
        this.nome = nome;
        this.data = data;
        cards = new ArrayList<>();
    }

    public Deck(User user) {
        this.user = user;
    }
    
    public void addCard(Card c) {
        cards.add(c);
    }
    
    public void salvarDeck() {
        try{
            File file = new File("deck.txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter pr = new PrintWriter(br);
            
            pr.print(this.getUser().getNome() + ";" + this.getNome() + ";"
                    + this.getData().getDataFormatada() + ";" + this.getVezesEstudadas() + System.getProperty("line.separator"));
            pr.close();
            br.close();
            fr.close();
            
        }catch(Exception e) {
            System.out.println("Erro ao escrever no arquivo!");
        }
    }
    
    // apagar informações do deck no arquivo deck.txt
    public void apagarDeck(int index) {
        try{
            File deckFile = new File("deck.txt");
            File temp = new File("temp.txt");
            temp.createNewFile();
            
            PrintStream psTemp = new PrintStream(temp);
            Scanner scanDeck = new Scanner(deckFile);
            String currentLine;
            int count = 0;
            // imprimir no arquivo temp.txt sem o usuário deletado
            while (scanDeck.hasNext()) {
                currentLine = scanDeck.nextLine();
                if (count != index) // pular usuário excluído
                    psTemp.print(currentLine + System.getProperty("line.separator"));
                ++count;
            }
            Scanner scanTemp = new Scanner(temp);
            PrintStream psUser = new PrintStream(deckFile);
            
            // copiar de temp.txt para user.txt
            while (scanTemp.hasNext()) {
                currentLine = scanTemp.nextLine();
                psUser.print(currentLine + System.getProperty("line.separator"));
                ++count;
            }
            
            psTemp.close();
            psUser.close();
            scanTemp.close();
            scanDeck.close();
            temp.delete();  // deletar arquivo temporário

        }catch(IOException e) {
            System.out.println("Erro ao apagar deck!");
        }
    }
    
    // carregar os dados do arquivo deck.txt
    public boolean carregarDeck(int index) {
        int contador = 0;
        
        try{
            FileInputStream arq = new FileInputStream("deck.txt");
            InputStreamReader input = new InputStreamReader(arq);
            BufferedReader br = new BufferedReader(input);
            
            String linha;
            Data dataAuxiliar = new Data();
            
            do {
                linha = br.readLine();
                if(contador == index && linha != null) {
                    String[] divisoes = linha.split(";");
                    if (divisoes[0].equals(this.user.getNome())) {
                        // Carregar os atributos de deck separadamente:
                        this.setNome(divisoes[1]);
                        /*Adicionando data de criação: */
                        dataAuxiliar.setDataComString(divisoes[2]);
                        this.setData(dataAuxiliar);
                        this.getData().imprimirData();
                        this.cards = new ArrayList();
                        this.setVezesEstudadas(Integer.parseInt(divisoes[3]));
                    }
                    arq.close();
                    input.close();
                    return true;
                }
                ++contador;
            }while(linha != null);
            
        }catch(Exception e) {
            System.out.println("Erro ao ler o arquivo!");
        }
                
        return false;
    }
    
    // renomeia o deck no arquivo deck.txt
    public void modificarDeck(String novoNome, int index, int tipo) {
        try{
            File deckFile = new File("deck.txt");
            File temp = new File("tempDeck.txt");
            temp.createNewFile();
            
            PrintStream psTemp = new PrintStream(temp);
            Scanner scanDeck = new Scanner(deckFile);
            String currentLine;
            int count = 0;
            
            // imprimir no arquivo temp.txt com as modificações feitas
            while (scanDeck.hasNext()) {
                currentLine = scanDeck.nextLine();
                if (count != index)
                    psTemp.print(currentLine + System.getProperty("line.separator"));
                else {
                    if(tipo == 1) {
                        String[] divisores = currentLine.split(";");
                        psTemp.print(divisores[0] + ";" + novoNome + ";" + 
                            divisores[2] + ";" + divisores[3] + System.getProperty("line.separator"));
                
                    } else if(tipo == 2) {
                        String[] divisores = currentLine.split(";");
                        psTemp.print(divisores[0] + ";" + divisores[1] + ";" + 
                            divisores[2] + ";" + novoNome + System.getProperty("line.separator"));
                    }
                }
                ++count;
            }
            //          nomeAntigo, novoNome
            if(tipo == 1)
                atualizarCard(getNome(), novoNome);
            
            Scanner scanTemp = new Scanner(temp);
            PrintStream psUser = new PrintStream(deckFile);
            
            // copiar de temp.txt para user.txt
            while (scanTemp.hasNext()) {
                currentLine = scanTemp.nextLine();
                psUser.print(currentLine + System.getProperty("line.separator"));
                ++count;
            }
            
            psTemp.close();
            psUser.close();
            scanTemp.close();
            scanDeck.close();
            temp.delete();  // deletar arquivo temporário

        }catch(IOException e) {
            System.out.println("Erro ao apagar deck!");
        }
    }
    
    // atualizar arquivo card.txt com respeito ao nome do deck
    private void atualizarCard(String antigoNome, String novoNome) {
        try{
            File cardFile = new File("card.txt");
            File temp = new File("tempCard.txt");
            temp.createNewFile();
            
            PrintStream psTemp = new PrintStream(temp);
            Scanner scanUser = new Scanner(cardFile);
            String currentLine;

            // imprimir no arquivo temp.txt sem o usuário deletado
            while (scanUser.hasNext()) {
                currentLine = scanUser.nextLine();
                String[] divisoes = currentLine.split(";");
                System.out.println(divisoes[1]);
                
                if (divisoes[1].equals(antigoNome)) { // copiar
                    psTemp.print(divisoes[0] + ";" + novoNome + ";" + divisoes[2] + ";" + divisoes[3] + ";"
                            + divisoes[4] + ";" + divisoes[5] + ";" + divisoes[6] + ";" + System.getProperty("line.separator"));
                }
                else
                    psTemp.print(currentLine + System.getProperty("line.separator"));
            }
            
            Scanner scanTemp = new Scanner(temp);
            PrintStream psUser = new PrintStream(cardFile);
            
            // copiar de temp.txt para user.txt
            while (scanTemp.hasNext()) {
                currentLine = scanTemp.nextLine();
                psUser.print(currentLine + System.getProperty("line.separator"));
            }
            
            psTemp.close();
            psUser.close();
            scanTemp.close();
            scanUser.close();
            temp.delete();  // deletar arquivo temporário

        }catch(IOException e) {
            System.out.println("Erro ao modificar usuário!");
        }
    }
    
    public ArrayList<Card> getCards() {
        return this.cards;
    }
    
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }  
    
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Data getData() {
        return data;
    }
    
    public void setData(Data data) {
        this.data = data;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }    

    public int getVezesEstudadas() {
        return VezesEstudadas;
    }

    public void setVezesEstudadas(int VezesEstudadas) {
        this.VezesEstudadas = VezesEstudadas;
    }

    public boolean isCarregouDados() {
        return carregouDados;
    }

    public void setCarregouDados(boolean carregouDados) {
        this.carregouDados = carregouDados;
    }

    public int getIndiceDoDeck() {
        return indiceDoDeckAtual;
    }

    public void setIndiceDoDeckAtual(int indiceDoDeck) {
        this.indiceDoDeckAtual = indiceDoDeck;
    }
    
    
}
