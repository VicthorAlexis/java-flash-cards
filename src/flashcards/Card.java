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
public class Card {
    private String frente;
    private String verso;
    private int numAcertos;
    private int numErros;
    
    private Data data;

    // Referências às outras classes
    Deck deck;

    public Card(String frente, String verso, Data data) {
        this.frente = frente;
        this.verso = verso;
        this.numAcertos = 0;
        this.numErros = 0;
        this.data = data;
    }
    
    public Card(Deck deck) {
        this.deck = deck;
    }
    
    public float aproveitamento() {
        if (numErros + numAcertos > 0)  // evitar divisao por zero
            return (float)numAcertos/(float)(numErros + numAcertos);
        return 0.0f;
    }
    
    public void salvarCard() {
        try{
            File file = new File("card.txt");
            if (!file.exists()) file.createNewFile();
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter pr = new PrintWriter(br);
            
            pr.print(getDeck().getUser().getNome() + ";" + getDeck().getNome()+ ";" + getFrente() + ";" + getVerso() + ";"
                    + getNumAcertos() + ";" + getNumErros() + ";" + getData().getDataFormatada() + System.getProperty("line.separator"));
            pr.close();
            br.close();
            fr.close();
            
        }catch(Exception e) {
            System.out.println("Erro ao escrever no arquivo!");
        }
    }
    
    public void apagarCard(int index) {
        try{
            File cardFile = new File("card.txt");
            File temp = new File("temp.txt");
            
            PrintStream psTemp = new PrintStream(temp);
            Scanner scanDeck = new Scanner(cardFile);
            String currentLine;
            int count = 0;
            // imprimir no arquivo temp.txt sem o card deletado
            while (scanDeck.hasNext()) {
                currentLine = scanDeck.nextLine();
                String[] divisores = currentLine.split(";");
                
                if (divisores[1].equals(deck.getNome()) == false) index += 1;
                
                if (count != index) // pular card excluído
                    psTemp.print(currentLine + System.getProperty("line.separator"));
                ++count;
            }
            Scanner scanTemp = new Scanner(temp);
            PrintStream psUser = new PrintStream(cardFile);
            
            // copiar de temp.txt para card.txt
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
            System.out.println("Erro ao apagar card!");
        }
    }
    
    public boolean carregarCard(int index) {
        int contador = 0;
        
        try{
            File file = new File("card.txt");
            if (!file.exists()) file.createNewFile();
            FileInputStream arq = new FileInputStream(file);
            InputStreamReader input = new InputStreamReader(arq);
            BufferedReader br = new BufferedReader(input);
            
            String linha;
            Data dataAuxiliar = new Data();
            
            do {
                linha = br.readLine();
                if (linha != null) {
                    String[] divisores = linha.split(";");
                    if (divisores[1].equals(deck.getNome()) == false) index += 1;
                    if(contador == index) {
                        // verifica se pertence ao usuário e ao deck
                        if (divisores[0].equals(deck.getUser().getNome())
                                && divisores[1].equals(deck.getNome())) {
                            // Carregar os atributos de deck separadamente:
                            this.setFrente(divisores[2]);
                            this.setVerso(divisores[3]);
                            this.setNumAcertos(Integer.parseInt(divisores[4]));
                            this.setNumErros(Integer.parseInt(divisores[5]));
                            /*Adicionando data de criação: */
                            dataAuxiliar.setDataComString(divisores[6]);
                            this.setData(dataAuxiliar);
                            this.getData().imprimirData();
                        }
                        arq.close();
                        input.close();
                        return true;
                    }
                }
                ++contador;
            }while(linha != null);
            
        }catch(Exception e) {
            System.out.println("Erro ao ler o arquivo!");
        }
                
        return false;
    }
    
    // copia as informações atuais do card para card.txt
    public void atualizaArquivo(int index) {
        try{
            File cardFile = new File("card.txt");
            File temp = new File("tempCard.txt");
            temp.createNewFile();
            
            PrintStream psTemp = new PrintStream(temp);
            Scanner scanDeck = new Scanner(cardFile);
            String currentLine;
            int count = 0;
            
            // imprimir no arquivo temp.txt com as modificações feitas
            while (scanDeck.hasNext()) {
                currentLine = scanDeck.nextLine();
                String[] divisores = currentLine.split(";");
                
                if (divisores[1].equals(deck.getNome()) == false) index += 1;
                
                if (count != index)
                    psTemp.print(currentLine + System.getProperty("line.separator"));
                else {
                    psTemp.print(getDeck().getUser().getNome() + ";" + getDeck().getNome()+ ";" + getFrente() + ";" + getVerso() + ";"
                        + getNumAcertos() + ";" + getNumErros() + ";" + getData().getDataFormatada() + System.getProperty("line.separator"));
                }
                ++count;
            }
            Scanner scanTemp = new Scanner(temp);
            PrintStream psUser = new PrintStream(cardFile);
            
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
    
    public Deck getDeck() {
        return deck;
    }

    public String getFrente() {
        return frente;
    }

    public void setFrente(String frente) {
        this.frente = frente;
    }

    public String getVerso() {
        return verso;
    }

    public void setVerso(String verso) {
        this.verso = verso;
    }

    public int getNumAcertos() {
        return numAcertos;
    }

    public void setNumAcertos(int numAcertos) {
        this.numAcertos = numAcertos;
    }

    public int getNumErros() {
        return numErros;
    }

    public void setNumErros(int numErros) {
        this.numErros = numErros;
    }

    public Data getData() {
        return data;
    }
    
    public void setData(Data data) {
        this.data = data;
    }
    
    public void setDeck(Deck deck) {
        this.deck = deck;
    }
    
}
