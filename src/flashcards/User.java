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
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    public User(){
        
    }
    
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
    
    public void addDeck(Deck deck) {
        decks.add(deck);
    }
    
    public boolean carregarUser(int index, User user) {
        
        int contador = 0;
        
        try{
            FileInputStream arq = new FileInputStream("user.txt");
            InputStreamReader input = new InputStreamReader(arq);
            BufferedReader br = new BufferedReader(input);
            
            String linha = br.readLine();
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
                    return true;
                }
                ++contador;
            }while(linha != null);
   
        }catch(Exception e) {
            System.out.println("Erro ao ler o arquivo!");
        }
        
        return false;
    }

    public void salvarUser(User user) {
        try{
            File file = new File("user.txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter pr = new PrintWriter(br);
            
            pr.println(user.getNome() + ";" + user.getSenha() + ";" + user.getData().imprimirData());
            pr.close();
            br.close();
            fr.close();
            
        }catch(Exception e) {
            System.out.println("Erro ao escrever no arquivo!");
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
    
}
