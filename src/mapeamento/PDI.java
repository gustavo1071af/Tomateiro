/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeamento;

import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

/**
 *
 * @author coordenador
 */
public class PDI extends Imagem {
    private Arquivo arq;
    private Tomates reg;
    private final String newline = System.lineSeparator();
    
    
    PDI () {
        arq = new Arquivo();
        reg = new Tomates();
    }
    
    PDI (Arquivo arquivo) {
        arq = arquivo;
        reg = new Tomates();
       
    }

    //pronto
    public void reduzDefinicaoLote(String[] imagens, JTextArea ap1, JProgressBar barra, JLabel label_Barra) {
        int teste;
        String path = arq.getPath();
       //configurando a barra
        barra.setMinimum(0);
        barra.setMaximum(imagens.length);
        
        
        boolean flag = (new File(path + "\\REDUZ")).mkdir();
        ap1.append("Path selecionado para salvar as imagens reduzidas: "+path + "\\REDUZ"+newline);
        ap1.setCaretPosition(ap1.getDocument().getLength());//cursoor ir para o final
        String[] arqsDiretorio = arq.abreArquivos(arq.getPath());
        for (int i = 0; i < imagens.length; i++) {
            if (verificarExistencia(arqsDiretorio, imagens[i])){
                teste = imagens[i].lastIndexOf(".");
                // TROCAR ISSO DEPOIS PARA VERIFICAR A EXTENSÃO E SÓ ACEITAR AS DE IMAGENS (JPG, ETC).
                // POSSO FAZER UMA ESCOLHA DO TIPO DE IMAGEM EM ALGUM LUGAR DO PROGRAMA, USANDO CHECKBOXES
                 if ((teste<0) || (imagens[i] == "Thumbs.db")){
                     continue;  // pula o subdiretório para onde irão as imagens
                 }
                 PlanarImage imagem = JAI.create("fileload", path + "\\" + imagens[i]);//será trocado depois a forma de escolher extensão.
                 //ap1.append("Salva a imagem reduzida em: "+path + "\\REDUZ\\" + imagens[i]+newline);
                 System.out.println(path + "\\REDUZ\\" + imagens[i]);
                 float scale = 0.3f;
                 ParameterBlock pb = new ParameterBlock();
                 pb.addSource(imagem);
                 pb.add(scale);
                 pb.add(scale);
                 pb.add(0.0F);
                 pb.add(0.0F);
                 pb.add(new InterpolationNearest());
                 PlanarImage reescalada = JAI.create("scale", pb);

                 // Salva a imagem gerada.
                 JAI.create("filestore", reescalada, path + "\\REDUZ\\"+imagens[i], "JPEG", null);
                 ap1.append("Salva a imagem reduzida em: "+path + "\\REDUZ\\" + imagens[i]+newline);
                 ap1.setCaretPosition(ap1.getDocument().getLength());//cursoor ir para o final
                 
                 //alterando a barra e o label da barra
                 barra.setValue(barra.getValue()+1);           
                 double porc = (barra.getValue()/Double.parseDouble(Integer.toString(imagens.length))*100);
                 label_Barra.setText("Redução das Imagens: "+ Math.round(porc)+"%");
              
                  
            }
            else{
                //alterando a barra e o label da barra
                 barra.setValue(barra.getValue()+1);           
                 double porc = (barra.getValue()/Double.parseDouble(Integer.toString(imagens.length))*100);
                 label_Barra.setText("Redução das Imagens: "+ Math.round(porc)+"%");
                 
                System.out.println("Não tem o arquivo: "+imagens[i]);
                 ap1.append("A IMAGEM '"+imagens[i]+"' NÃO FOI ENCONTRADA, PORTANTO NÃO SERÁ REDUZIDA!"+newline);
                 ap1.setCaretPosition(ap1.getDocument().getLength());//cursoor ir para o final
                 
            }
       }
       ap1.append("Redução das imagens terminada."+newline+"*************"+newline);
       ap1.setCaretPosition(ap1.getDocument().getLength());//cursoor ir para o final
       arq.setPath(arq.getPath()+"\\REDUZ");
       
    } 
        //função para gerar qrquivo txt SE CASO NECESSITAR DE UM LOG
    /*public void geraArquivoTxt(String[] imagens, int tpPDI,
            double tol1, double tol2, double nR, double nG, double nB) {
        int teste;
        long[][] minMax = new long[12][2];
        String path = arq.getPath();

        System.out.println(path);
        boolean flag = (new File(path + "\\CHAVES")).mkdir();
        arq.criaArquivoTxtOut(path + "\\CHAVES\\folhasChave.txt");
        int cont = 0;

        for (int i = 0; i < imagens.length; i++) {
            teste = imagens[i].lastIndexOf(".");

            // TROCAR ISSO DEPOIS PARA VERIFICAR A EXTENSÃO E SÓ ACEITAR AS DE IMAGENS (JPG, ETC).
            // POSSO FAZER UMA ESCOLHA DO TIPO DE IMAGEM EM ALGUM LUGAR DO PROGRAMA, USANDO CHECKBOXES
            if ((teste < 0) || (imagens[i] == "Thumbs.db")) {
                continue;  // pula o subdiretório para onde irão as imagens
            }

            System.out.println(imagens[i]);
            processaImagemChaveada(imagens[i], tol1, tol2, nR, nG, nB);
            arq.adicionaRegistros(reg);
        }

       
       
    }*/
    
    
    //pronto
     public void processarEArmazernar(String[] imagens, int tpPDI,
            double tol1, double tol2, double nR, double nG, double nB, JTextArea ap1, JTextArea ap2, JProgressBar barra, JLabel label_Barra) {
        int teste;
       //configurando a barra
        barra.setValue(0);
        barra.setMinimum(0);
        barra.setMaximum(imagens.length * 2);//multiplicado por 2 porque são duas coisas, processo e armazenamento de cada imagem
       
        //fazer a query chamando os tomateiros que não tem imagem processada para comparar com o nome das
        //imagens do diretório para fazer o processo 
        ap1.append("Iniciando o processamento das imagens e salvando no BD..."+newline);
        ap1.setCaretPosition(ap1.getDocument().getLength());//cursoor ir para o final
        ap1.append("Path selecionado para salvar as imagens processdas: "+arq.getPath() + "\\CHAVES"+newline);
        ap1.setCaretPosition(ap1.getDocument().getLength());//cursoor ir para o final
        String sql= "SELECT * FROM tomate t where not exists(select * from imagem_processada ip where t.numtom = ip.Tomate_numtom  and t.rua = ip.Tomate_rua  and t.linha = ip.Tomate_linha  and t.data = ip.Tomate_data)";
        ///fazer conexao com o banco 
        Connection con = new Conn().getConnection();
        try {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        int cont = 0;
        //processando cada registro encontrado na query
       
            while (rs.next()) {
                cont++;
                
                //alterando a barra e o label da barra
                 barra.setValue(barra.getValue()+1);
                 double porc = (barra.getValue()/Double.parseDouble(Integer.toString(imagens.length*2))*100);
                 label_Barra.setText("Processando e Armazenando: "+ Math.round(porc)+"%");
                 
                //preencher registro
                reg.setData(rs.getString("data"));
                reg.setRua(Integer.parseInt(rs.getString("rua")));
                reg.setLinha(rs.getString("linha"));
                reg.setNumTom(Integer.parseInt(rs.getString("numtom")));
                String nomeimagem = rs.getString("nomearquivo");//nome do arquivo de cada tomate achado na query acima adicionada a extensão
                if (verificarExistencia(imagens, nomeimagem)) {//verificar se o nome do arquivo existe na pasta.

                    teste = nomeimagem.lastIndexOf(".");
                    //vou fazer>>gustavo
                    // TROCAR ISSO DEPOIS PARA VERIFICAR A EXTENSÃO E SÓ ACEITAR AS DE IMAGENS (JPG, ETC).
                    // POSSO FAZER UMA ESCOLHA DO TIPO DE IMAGEM EM ALGUM LUGAR DO PROGRAMA, USANDO CHECKBOXES
                    if ((teste < 0) || (nomeimagem == "Thumbs.db")) {
                        continue;  // pula o subdiretório para onde irão as imagens

                    }

                    System.out.println(nomeimagem);
                    
                    processaImagemChaveada(nomeimagem, tol1, tol2, nR, nG, nB, ap1);
                    
                    //alterando a barra e o label da barra
                    barra.setValue(barra.getValue()+1);
                    double porc2 = (barra.getValue()/Double.parseDouble(Integer.toString(imagens.length*2))*100);
                    label_Barra.setText("Processando e Armazenando: "+ Math.round(porc2)+"%");
                    adicionaRegistros(reg, ap2);
                    
                    
                    //System.out.println();
                } else {//se não encontrar a imagem no diretório
                    //alterando a barra e o label da barra
                    barra.setValue(barra.getValue()+2);// porque pula o processamento e o armazenamento por não achar a imagem no diretório
                    double porc3 = (barra.getValue()/Double.parseDouble(Integer.toString(imagens.length*2))*100);
                    label_Barra.setText("Processando e Armazenando: "+ Math.round(porc3)+"%");
                    
                    System.out.println("Não achou no diretório o arquvo " + nomeimagem);
                    ap2.append("A IMAGEM '" + nomeimagem + "' NÃO FOI ENCONTRADA NO DIRETÓRIO //REDUZ, PORTANTO É IMPOSSÍVEL SER PROCESSADA!" + newline + "***********" + newline);
                    ap2.setCaretPosition(ap2.getDocument().getLength());//cursoor ir para o final

                }

            }//fechar o while

        //closenaconexao
            con.close();
        } catch (SQLException e) {
            System.out.println("Erro no SQL:" + e.getMessage());
        }
    }
     
     
     //pronto
    public boolean verificarExistencia(String[] imagens, String nomeimagem) {
        for (int i = 0; i < imagens.length; i++) {
            
            if (imagens[i].equalsIgnoreCase(nomeimagem)) {
               System.out.println(imagens[i]+" =? "+nomeimagem);
                return true;
            }
        }
        return false;
    }
    
    public String[] verificaNecessidade(JTextArea ap1) {
        String [] listaAuxiliar = null; 
         String sql= "SELECT nomearquivo FROM tomate t where not exists(select * from imagem_processada ip where t.numtom = ip.Tomate_numtom  and t.rua = ip.Tomate_rua  and t.linha = ip.Tomate_linha  and t.data = ip.Tomate_data)";
        ///fazer conexao com o banco 
        Connection con = new Conn().getConnection();
        try {
            Statement stmt3 = con.createStatement();
            ResultSet rs3 = stmt3.executeQuery(sql);
            //processando cada registro encontrado na query
            rs3.last();//colocar ponteiro para ultimo registro do resulset
            int numlinha = rs3.getRow();
            rs3.beforeFirst();
            if(numlinha>0){//verificar se existe registros
                listaAuxiliar=new String[numlinha];
                ap1.append("Iniciando o processo..."+newline);
                ap1.append("Foram encontrados "+numlinha+" registros que ainda não tiveram as imagens processadas processados."+newline);
               
                
               //fazer a lista de imagens que serão processadas
                
                int cont=0;
                
                while(rs3.next()){
                    listaAuxiliar[cont] = rs3.getString("nomearquivo");
                    System.out.println(cont+": "+listaAuxiliar[cont]);
                    cont++;
                }
                
                con.close();
                return listaAuxiliar;
            }
        } catch(SQLException e) {
            System.out.println("Erro na verificação de necessidade:" + e.getMessage());
            //depois mudar cor das mensagens de erro com highlights
             ap1.append("Falha no banco de dados ao verificar a necessidade do processamento de imagens..."+newline);
             ap1.setCaretPosition(ap1.getDocument().getLength());//cursoor ir para o final
        }
        
        return listaAuxiliar;
        
    }
    
//pronto
    public void processaImagemChaveada (String imagem, 
            double tol1, double tol2, double nR, double nG, double nB, JTextArea ap1) {
        int teste;
        String path = arq.getPath();
   
        System.out.println("Path no processamento: " +arq.getPath());
        
        boolean flag = (new File(path + "\\CHAVES")).mkdir();
       
        teste = imagem.lastIndexOf(".");
        // TROCAR ISSO DEPOIS PARA VERIFICAR A EXTENSÃO E SÓ ACEITAR AS DE IMAGENS (JPG, ETC).
        // POSSO FAZER UMA ESCOLHA DO TIPO DE IMAGEM EM ALGUM LUGAR DO PROGRAMA, USANDO CHECKBOXES
        if ((teste < 0) || (imagem == "Thumb.db")) {
            return;  // pula o subdiretório para onde irão as imagens
        }
        ap1.append("Processando a imagem: " +imagem+"..."+newline);
        ap1.setCaretPosition(ap1.getDocument().getLength());//cursoor ir para o final
        System.out.println(imagem);
         ap1.append("Carregando a imagem reduzida em: "+ path + "\\" + imagem+newline);
        ap1.setCaretPosition(ap1.getDocument().getLength());//cursoor ir para o final
        PlanarImage imgP = JAI.create("fileload", path + "\\" + imagem);
        int largura = imgP.getWidth();
        int altura = imgP.getHeight();
        SampleModel sm = imgP.getSampleModel();
        int nbandas = sm.getNumBands();
        Raster rasterLeitura = imgP.getData();
        WritableRaster rasterEscrita = rasterLeitura.createCompatibleWritableRaster();
        int[] pixels = new int[nbandas * largura * altura];
        rasterLeitura.getPixels(0, 0, largura, altura, pixels);

        int deslocamento, totR = 0, totG = 0, totP = 0, tot = 0;
        int rMD = 0, gMD = 0, bMD = 0;

        for (int a = 0; a < altura; a++) {
            for (int l = 0; l < largura; l++) {
                //deslocamento = 1;
                deslocamento = a * largura * nbandas + l * nbandas;
                rMD = rMD + pixels[deslocamento + 0];
                gMD = gMD + pixels[deslocamento + 1];
                bMD = bMD + pixels[deslocamento + 2];
                tot++;

                // vou destacar os pixels em tons de verde, tornando-os verde puro
                if ((pixels[deslocamento + 0] >= 25 * (1 - nR)) && (pixels[deslocamento + 0] <= 120 * (1 + nR))
                        && (pixels[deslocamento + 1] >= 60 * (1 - nG)) && (pixels[deslocamento + 1] <= 160 * (1 + nG))
                        && (pixels[deslocamento + 2] >= 25 * (1 - nB)) && (pixels[deslocamento + 2] <= 100 * (1 + nB))
                        && (pixels[deslocamento + 1] >= pixels[deslocamento + 0] * (1 - tol2))
                        && (pixels[deslocamento + 1] >= pixels[deslocamento + 2] * (1 - tol2))
                        && ((pixels[deslocamento + 0] + pixels[deslocamento + 2]) * tol1 <= pixels[deslocamento + 1])) {
                    pixels[deslocamento + 0] = 0;
                    pixels[deslocamento + 1] = 255;
                    pixels[deslocamento + 2] = 0;
                    totG++;
                } else // vou destacar os pixels em tons de amarelo, tornando-os vermelhos puro
                if ((pixels[deslocamento + 0] >= 135 * (1 - nR)) && (pixels[deslocamento + 0] <= 180 * (1 + nR))
                        && (pixels[deslocamento + 1] >= 130 * (1 - nG)) && (pixels[deslocamento + 1] <= 160 * (1 + nG))
                        && (pixels[deslocamento + 2] >= 40 * (1 - nB)) && (pixels[deslocamento + 2] <= 60 * (1 + nB))) {
                    pixels[deslocamento + 0] = 255;
                    pixels[deslocamento + 1] = 0;
                    pixels[deslocamento + 2] = 0;
                    totR++;
                } else //PRECISO DESCOBRIR AS PROPORÇÕES IDEIAS PARA MUDANÇA DE COR!!!!!
                // vou destacar os pixels em tons de marrom, tornando-os vermelho puro
                if ((pixels[deslocamento + 0] >= 10 * (1 - nR)) && (pixels[deslocamento + 0] <= 120 * (1 + nR))
                        && (pixels[deslocamento + 1] >= 25 * (1 - nG)) && (pixels[deslocamento + 1] <= 110 * (1 + nG))
                        && (pixels[deslocamento + 2] >= 10 * (1 - nB)) && (pixels[deslocamento + 2] <= 95 * (1 + nB))) {
                    pixels[deslocamento + 0] = 255;
                    pixels[deslocamento + 1] = 0;
                    pixels[deslocamento + 2] = 0;
                    totR++;
                } // vou destacar os pixels do fundo, tornando-os pretos
                else {
                    pixels[deslocamento + 0] = 0;
                    pixels[deslocamento + 1] = 0;
                    pixels[deslocamento + 2] = 0;
                    totP++;
                }
            }
        }

        rasterEscrita.setPixels(0, 0, largura, altura, pixels);
        TiledImage ti = new TiledImage(imgP, 1, 1);
        ti.setData(rasterEscrita);
        
        
        JAI.create("filestore", ti, path + "\\CHAVES\\COR_" + imagem, "JPEG", null);
        
        ap1.append("Salva a imagem processada em:" +path + "\\CHAVES\\COR_" + imagem+newline);
        ap1.setCaretPosition(ap1.getDocument().getLength());//cursoor ir para o final

        reg.setVermelhos(totR);
        reg.setVerdes(totG);
        reg.setPretos(totP);
        reg.setNomeArquivo(imagem);
        setGradKey((double) totR / totG * 100);
        reg.setEstado(getGradKey());
        ap1.append("Finalizando o processamento e gerando o resultado."+newline+"***********"+newline);
        ap1.setCaretPosition(ap1.getDocument().getLength());//cursoor ir para o final
        System.out.println(totR + " " + totG + " " + totP + " "
                + (double) totR / totG * 100 + " " + getGradKey() + "%");
 
    }

    //em desenvolvimento
    private void adicionaRegistros(Tomates reg, JTextArea ap2) {
        System.out.println("Adicionando o registro: "+reg.getNomeArquivo()+", "+reg.getVermelhos()+", "+reg.getVerdes()+", "+reg.getPretos()+", "+reg.getEstado()+", "+reg.getNumTom()+", "+reg.getRua()+", "+reg.getLinha()+", "+reg.getData());
        ap2.append(">> Adicionando o registro no banco de dados da imagem: "+reg.getNomeArquivo()+" chaveada com o Estado: "+reg.getEstado()+"."+newline);
        ap2.setCaretPosition(ap2.getDocument().getLength());//cursoor ir para o final
        String comando2 = "INSERT INTO imagem_processada (nomearquivo, vermelhos, verdes, pretos, estado, Tomate_numtom, Tomate_rua, Tomate_linha, Tomate_data) VALUES ("
                +"'COR_"+ reg.getNomeArquivo() + "', "
                + reg.getVermelhos() + ", "
                + reg.getVerdes() + ", "
                + reg.getPretos() + ", "
                + reg.getEstado() + ", "
                + reg.getNumTom() + ", "
                + reg.getRua() + ", "
                +"'"+ reg.getLinha() + "', "
                +"'"+ reg.getData() + "');";

        
        
        Connection con = new Conn().getConnection();
        try {
            Statement stmt2 = con.createStatement();
            stmt2.execute(comando2);
            con.close();
        } catch (SQLException e) {
            System.out.println("Erro no addregistro:" + e.getMessage());
        }
        
   
    }
    
}
