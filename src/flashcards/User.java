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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Victhor-pc
 */
public class User {
    
    private String nome;
    private String senha;
    private Data data;
    ArrayList<Deck> decks;

    public User(){
        
    }
    
    public User(String nome, String senha, Data data) {
        this.nome = nome;
        this.senha = senha;
        this.data = data;
        decks = new ArrayList();
    }
    
    public void addDeck(Deck deck) {
        decks.add(deck);
    }
    
    public boolean carregarUser(int index, User user) {
        
        int contador = 0;
        
        try{
            FileInputStream arq = new FileInputStream("user.txt");
            InputStreamReader input = new InputStreamReader(arq);
            BufferedReader br = new BufferedReader(input);
            
            String linha;
            Data dataAuxiliar = new Data();
            
            do {
                linha = br.readLine();
                
                if(contador == index && linha != null) {
                    String[] divisoes = linha.split(";");
                    // Carregar os atributos de user separadamente:
                    user.setNome(divisoes[0]);
                    user.setSenha(divisoes[1]);
                    /*Adicionando data de criação: */
                    dataAuxiliar.setDataComString(divisoes[2]);
                    user.setData(dataAuxiliar);
                    user.getData().imprimirData();
                    user.decks = new ArrayList();
                    
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

    public void salvarUser() {
        try{
            File file = new File("user.txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fr);
            
            bw.write(this.getNome() + ";" + this.getSenha() + ";" + 
                    this.getData().imprimirData() + System.getProperty("line.separator"));
            
            bw.close();
            fr.close();
        }catch(IOException e) {
            System.out.println("Erro ao escrever no arquivo!");
        }
    }
    
    public void modificar(String nome, String senha, int index) {
        try{
            File userFile = new File("user.txt");
            File temp = new File("tempUser.txt");
            temp.createNewFile();
            
            PrintStream psTemp = new PrintStream(temp);
            Scanner scanUser = new Scanner(userFile);
            String currentLine;
            int count = 0;
            
            // imprimir no arquivo temp.txt com as modificações feitas
            while (scanUser.hasNext()) {
                currentLine = scanUser.nextLine();
                if (count != index)
                    psTemp.print(currentLine + System.getProperty("line.separator"));
                else {
                    psTemp.print(nome + ";" + senha + ";" + 
                        this.getData().imprimirData() + System.getProperty("line.separator"));
                }
                ++count;
            }
            //          nomeAntigo, novoNome
            atualizarDeck(getNome(), nome);
            atualizarCard(getNome(), nome);
            
            Scanner scanTemp = new Scanner(temp);
            PrintStream psUser = new PrintStream(userFile);
            
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
            System.out.println("Erro ao apagar usuário!");
        }
        
    }
    
    public void apagarUser(int index) {
        try{
            File userFile = new File("user.txt");
            File temp = new File("tempUser.txt");
            temp.createNewFile();
            
            PrintStream psTemp = new PrintStream(temp);
            Scanner scanUser = new Scanner(userFile);
            String currentLine;
            int count = 0;
            
            // imprimir no arquivo temp.txt sem o usuário deletado
            while (scanUser.hasNext()) {
                currentLine = scanUser.nextLine();
                if (count != index) // pular usuário excluído
                    psTemp.print(currentLine + System.getProperty("line.separator"));
                ++count;
            }
            Scanner scanTemp = new Scanner(temp);
            PrintStream psUser = new PrintStream(userFile);
            
            // copiar de temp.txt para user.txt
            while (scanTemp.hasNext()) {
                currentLine = scanTemp.nextLine();
                psUser.print(currentLine + System.getProperty("line.separator"));
                ++count;
            }
            
            psTemp.close();
            psUser.close();
            scanTemp.close();
            scanUser.close();
            temp.delete();  // deletar arquivo temporário

        }catch(IOException e) {
            System.out.println("Erro ao apagar usuário!");
        }
    }
    
    // atualizar arquivo deck.txt com respeito ao nome do usuário
    private void atualizarDeck(String antigoNome, String novoNome) {
        try{
            File deckFile = new File("deck.txt");
            File temp = new File("tempDeck.txt");
            temp.createNewFile();
            
            PrintStream psTemp = new PrintStream(temp);
            Scanner scanUser = new Scanner(deckFile);
            String currentLine;

            // imprimir no arquivo temp.txt sem o usuário deletado
            while (scanUser.hasNext()) {
                currentLine = scanUser.nextLine();
                String[] divisoes = currentLine.split(";");
                
                if (divisoes[0].equals(antigoNome)) // copiar se não
                    psTemp.print(novoNome + ";" + divisoes[1] + ";"
                            + divisoes[2] + System.getProperty("line.separator"));
                else
                    psTemp.print(currentLine + System.getProperty("line.separator"));
            }
            
            Scanner scanTemp = new Scanner(temp);
            PrintStream psUser = new PrintStream(deckFile);
            
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
    
    // atualizar arquivo card.txt com respeito ao nome do usuário
    private void atualizarCard(String antigoNome, String novoNome) {
        try{
            File deckFile = new File("card.txt");
            File temp = new File("tempCard.txt");
            temp.createNewFile();
            
            PrintStream psTemp = new PrintStream(temp);
            Scanner scanUser = new Scanner(deckFile);
            String currentLine;

            // imprimir no arquivo temp.txt sem o usuário deletado
            while (scanUser.hasNext()) {
                currentLine = scanUser.nextLine();
                String[] divisoes = currentLine.split(";");
                
                if (divisoes[0].equals(antigoNome)) // copiar
                    psTemp.print(novoNome + ";" + divisoes[1] + ";" + divisoes[2] + ";" + divisoes[3] + ";"
                            + divisoes[4] + ";" + divisoes[5] + ";" + divisoes[6] + System.getProperty("line.separator"));
                else
                    psTemp.print(currentLine + System.getProperty("line.separator"));
            }
            
            Scanner scanTemp = new Scanner(temp);
            PrintStream psUser = new PrintStream(deckFile);
            
            // copiar de temp.txt para card.txt
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

    public void setDecks(ArrayList<Deck> decks) {
        this.decks = decks;
    }   
}
