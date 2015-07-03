/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeamento;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author gizelle
 */

public class Registro {
    //private Campos[] linha;

    private long id;
    private String nome;
    private long pretos;
    private long vermelhos;
    private long verdes;
    private long pretosC;
    private int estado;

    public Registro() {
        this(0," ",0,0,0,0);           
    }
    
    public Registro(long iD,String nm,long vrms,long vds,long ptsC,int est) {
        
        id = iD;
        nome = nm;
        vermelhos = vrms;
        verdes = vds;
        pretosC = ptsC;
        estado = est;

    }
    
    public void setId(long id) {
        this.id = id;
    };
    
    public long getId() {
        return id;
    };
    
    public void setNome(String n) {
        nome = n;
    };
    
    public String getNome() {
        return nome;
    };
     
    public void setVermelhos (long v) {
        vermelhos = v;
    };
    
    public long getVermelhos () {
        return vermelhos;
    };
 
    public void setVerdes (long v) {
        verdes = v;
    };
    
    public long getVerdes () {
        return verdes;
    };
 
    public void setPretosC (long p) {
        pretosC = p;
    };
    
    public long getPretosC () {
        return pretosC;
    };
 
    public void setEstado (int e) {
        estado = e;
    };
    
    public int getEstado () {
        return estado;
    };
 
    public void geraTabelaBD(String path) {
        long[][] minMax = new long[12][2];
        Arquivo arq = new Arquivo();

        boolean flag = (new File(path + "\\CHAVES")).mkdir();
        arq.criaArquivoTxtIn(path + "\\CHAVES\\folhasChave.txt");
        Scanner input = arq.getInput();

        try {
            //Criando Tabelas
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Lasico", "root", "root");
            Statement stm = con.createStatement();
            stm.execute("DROP TABLE IF EXISTS FolhasChaveadas");

            stm = con.createStatement();
            String comando = "Create Table FolhasChaveadas (id BIGINT NOT NULL AUTO_INCREMENT ,nome CHAR(45) NOT NULL,"
                    + "vermelhos BIGINT NOT NULL, verdes BIGINT NOT NULL, pretosC BIGINT NOT NULL,"
                    + "estado INT, UNIQUE(id), PRIMARY KEY (id) ); ";
            stm.execute(comando);

            String linha, nome, vermelhos, verdes, pretosC, estado;
            int iNome, iVermelhos, iVerdes, iPretosC;

            linha = (input.nextLine());  // pula a linha de cabeçalho

            while (input.hasNext()) {//                id=rs.getString(1); 
                linha = (input.nextLine());
                iNome = linha.indexOf(';', 0);
                iVermelhos = linha.indexOf(';', iNome + 1);
                iVerdes = linha.indexOf(';', iVermelhos + 1);
                iPretosC = linha.indexOf(';', iVerdes + 1);

                nome = linha.substring(0, iNome);
                vermelhos = linha.substring(iNome + 1, iVermelhos);
                verdes = linha.substring(iVermelhos + 1, iVerdes);
                pretosC = linha.substring(iVerdes + 1, iPretosC);
                estado = linha.substring(iPretosC + 1, linha.length());

                //montando as tabelas auxiliares
                String insert = "Insert Into FolhasChaveadas (nome,vermelhos,verdes,pretosC,estado) Values"
                        + " (\'" + nome + "\'," + vermelhos + "," + verdes + "," + pretosC + "," + estado + ")";
                Statement stm2 = con.createStatement();
                stm2.execute(insert);
            }

            //Fechamento de conexão
            con.close();
        } catch (SQLException sqlex) {
            System.out.println("Erro de conexão: " + sqlex.getMessage());
        }
    }

    
}
