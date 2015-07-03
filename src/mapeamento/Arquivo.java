/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeamento;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author gizelle
 */
public class Arquivo extends javax.swing.JFrame {
    
    private String caminho;
    private String arquivo;
    private int entradas, saidas;
    private Formatter output;
    private Scanner input;
        
    public void setPath(String p) {
        caminho = p;
    }

    public void setArquivo(String arq) {
        arquivo = arq;
    }

    public String getPath() {
        return caminho;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setEntradas(int e) {
        entradas = e;
    };
    
    public int getEntradas () {
        return entradas;
    };
 
    public void setSaidas(int s) {
        saidas = s;
    };
    
    public int getSaidas () {
        return saidas;
    };
  
 
    public void setInput(Scanner i) {
        input = i;
    };
    
    public Scanner getInput () {
        return input;
    };
    
     
    public void setOutput(Formatter f) {
        output = f;
    };
    
    public Formatter getOutput () {
        return output;
    };
    
   public void renomeiaArquivos (String path,String[] imagens) {
        int teste;
        File arq1,arq2;
        String arq;
        
        boolean flag = (new File(path + "\\NOVOS")).mkdir();

        for (int i=0; i<imagens.length; i++ ) {
            
           teste = imagens[i].lastIndexOf(".");
           // TROCAR ISSO DEPOIS PARA VERIFICAR A EXTENSÃO E SÓ ACEITAR AS DE IMAGENS (JPG, ETC).
           // POSSO FAZER UMA ESCOLHA DO TIPO DE IMAGEM EM ALGUM LUGAR DO PROGRAMA, USANDO CHECKBOXES
            if (teste<0){
                continue;  // pula o subdiretório para onde irão as imagens
            }
            
           arq1 = new File(path + "\\"+imagens[i]);
           arq1.renameTo(new File(path + "\\NOVOS\\DNT_"+imagens[i]));
           
           System.out.println(imagens[i]);
     
        }
    }


    public String escolheDir() {
        JFileChooser fc = new JFileChooser();
        String arq = new String();

        // restringe a amostra a diretorios apenas
       /* fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int op = fc.showOpenDialog(this);
        if (op == JFileChooser.APPROVE_OPTION) {
            caminho = fc.getSelectedFile().getAbsolutePath();
            arq = fc.getSelectedFile().getName();
        }*/

        //setPath(caminho);
        setPath("C:\\Users\\Gus\\Documents\\NetBeansProjects\\Mapeamento\\src\\mapeamento\\fotos_tomates");
        return arq;
    }
    

   String abreArquivo(String path) {
        File dir = new File(path);
        String arq = dir.getName();

        return arq;
   }

   public void removeDir() {
        String msg = "Deseja realmente remover a pasta ";
        int resp;
        JFileChooser fc = new JFileChooser();
        
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int op = fc.showOpenDialog(this);
        if (op == JFileChooser.APPROVE_OPTION) {
            msg = msg + fc.getSelectedFile().getAbsolutePath() + "?";
        }

        resp = JOptionPane.showConfirmDialog(null, msg, "", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            if (fc.getSelectedFile().isDirectory()) {
                String[] children = fc.getSelectedFile().list();
                for (int i = 0; i < children.length; i++) {
                    (new File(fc.getSelectedFile(), children[i])).delete();
                }
            }
            fc.getSelectedFile().delete();
        }
    }
  
    String[] abreArquivos(String path) {
        File dir = new File(path);
        String[] children = dir.list();
        
        if (children == null) {
            // Either dir does not exist or is not a directory  
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory  
                String filename = children[i];
                System.out.println(children[i]);
            }
        }
        return children;
    }
  
    public void criaArquivoTxtIn (String path) {
        try {
            input = new Scanner(new File(path));
        }
        catch (FileNotFoundException fileNot) {
            System.err.println("Erro de acesso ao arquivo");
            System.exit(1);
        }
   }

   public void criaArquivoTxtOut (String path) {
        try {
            output = new Formatter(path);
            output.format("Nome;Vermelhos;Verdes;PretosC;Estado%n");
        }
        catch (SecurityException sec) {
            System.err.println("Erro de acesso ao arquivo");
            System.exit(1);
        }
        catch (FileNotFoundException fileNot) {
            System.err.println("Erro de acesso ao arquivo");
            System.exit(1);
        }
        
   }

   public void adicionaRegistros(Registro reg) {
       try {
           output.format("%s;%d;%d;%d;%d%n", 
                   reg.getNome(),reg.getVermelhos(),reg.getVerdes(),
                   reg.getPretosC(),reg.getEstado());
       }
       catch (FormatterClosedException formatter) {
           System.err.println("Erro de escrita em arquivo");
       }
   }
   
   public void fechaArqOut() {
       if (output != null)
           output.close();
   }
      
   public void fechaArqIn() {
       if (input != null)
           input.close();
   }

    
      
}
