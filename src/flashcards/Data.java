/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashcards;

/**
 *
 * @author Victhor
 */
public class Data {
    private int dia;
    private int mes;
    private int ano;
    private int hora; // tem que criar 4 atributos
    private String dataFormatada;
    
    public Data() {
        
    }
    
    public Data(int dia, int mes, int ano, int hora) {
        if(validarData(dia, mes, ano, hora)) {
            this.dia = dia;
            this.mes = mes;
            this.ano = ano;
            this.hora = hora;
        } else {
            this.dia = 0;
            this.mes = 0;
            this.ano = 0;
            this.hora = 0;
        }
        
        dataFormatada = this.dia + "/" + (this.mes+1) + "/" + this.ano;
    }
    
    public String escolherFormato(String pos1, String pos2, String pos3) {
        String pos[] = {pos1, pos2, pos3}; 
        int contador = 0;
        int p[] = null;
        
        for(int i = 0; i < 3; ++i) {
            
            // Verificando se as entradas são válidas e se são diferentes umas das outras (válidas: dia, mes, ano)
            
            if(pos[i].equals("dia") && !pos[i].equals(pos[i-1])) {
                p[i] = dia;
                ++contador;
            }
            
            if(pos[i].equals("mes") && !pos[i].equals(pos[i-1])) {
                p[i] = mes;
                ++contador;
            }
            
            if(pos[i].equals("ano") && !pos[i].equals(pos[i-1])) {
                p[i] = ano;
                ++contador;
            }
            
        }
        
        if(contador == 3)
            dataFormatada = p[0] + "/" + p[1] + "/" + p[2];
        
        return dataFormatada;
    }// Exemplo de entrada na função: mes, ano, dia
    
    private boolean validarData(int dia, int mes, int ano, int hora) {
        return (dia > 0 && dia <= 31 && mes >0 && mes <= 12 && hora >= 0 && hora <= 23 && ano >= 0);
    } // Caso a data seja adicionada manualmente.
    
    public void setDataComString(String dataEmString) {
        this.dia = Integer.parseInt(dataEmString.substring(0, 2));
        this.mes = Integer.parseInt(dataEmString.substring(3, 5));
        this.ano = Integer.parseInt(dataEmString.substring(6, 10));
        
        this.dataFormatada = this.dia + "/" + (this.mes+1) + "/" + this.ano;
    }// formato: DD/MM/AAAA
    
    public String imprimirData() {
        return dataFormatada;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public String getDataFormatada() {
        return dataFormatada;
    }

    public void setDataFormatada(String dataFormatada) {
        this.dataFormatada = dataFormatada;
    }
    
    
}
