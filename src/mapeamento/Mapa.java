/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeamento;

import java.awt.Color;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Gus
 */
public class Mapa extends javax.swing.JPanel {

    int tam;
    MeuJPanel matrizpainel[][];

    /**
     * Creates new form Mapa
     */
    public Mapa() {
        initComponents();
        String sql = "SELECT * \n"
                + "FROM tomate t, imagem_processada i\n"
                + "WHERE t.rua = i.Tomate_rua\n"
                + "AND t.linha = i.Tomate_linha\n"
                + "AND t.numtom = i.Tomate_numtom\n"
                + "AND t.data = i.Tomate_data "
                + "ORDER BY LPAD( t.rua, 4,  '0' ) asc, t.linha asc, lpad( t.numtom, 4,  '0' ) asc";
        Connection con = new Conn().getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // chamar tamanho do vetor
            //transformar vetor em uma matriz aproximada do resultado de Math.round(Math.sqrt(tamanho do vetor))
            //Chamar vetor de tomates
            rs.last();
            //arredondando para cima
            int tam = (int) Math.round(((double) Math.sqrt(rs.getRow())) + 0.5d);
            //System.out.println((int)Math.round(((double)Math.sqrt(rs.getRow()))+0.5d));
            rs.beforeFirst();

            
            //aplica o resultado para fazer o grid
           
            matrizpainel = new MeuJPanel[tam][tam];

            GridLayout grid = new GridLayout(tam, tam);
            setLayout(grid);
            for (int x = 0; x < tam; x++) {
                for (int y = 0; y < tam; y++) {

                    matrizpainel[x][y] = new MeuJPanel();
                    matrizpainel[x][y].setBackground(new Color(192, 192, 192));//Silver

                    matrizpainel[x][y].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

                   
                    if (rs.next()) {
                         //começando a criar a classe tomates para enviar ao dialog
                        Tomates tom = new Tomates();
                        tom.setNomeArquivo(rs.getString("nomearquivo"));
                        tom.setNumTom(Integer.parseInt(rs.getString("numtom")));
                        tom.setRua(Integer.parseInt(rs.getString("rua")));
                        tom.setLinha(rs.getString("linha"));
                        tom.setData(rs.getString("data"));
                        tom.setLongi(rs.getString("longi"));
                        tom.setLat(rs.getString("lat"));
                        tom.setVermelhos(Integer.parseInt(rs.getString("vermelhos")));
                        tom.setVerdes(Integer.parseInt(rs.getString("verdes")));
                        tom.setEstado(Integer.parseInt(rs.getString("estado")));
                        //colocar aqui os eventos.
                        final MeuJPanel painelAux = matrizpainel[x][y];
                        painelAux.setTom(tom);
                        matrizpainel[x][y].addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                    
                                    final JFrame parent = new JFrame();
                                    TomateDialog t1 = new TomateDialog(parent, true, painelAux.getTom());
                                    t1.setSize(800, 600);
                                    //System.out.println(painelAux.getTom().getNomeArquivo());
                                    t1.setTitle("Rua="+painelAux.getTom().getRua()+" Linha="+painelAux.getTom().getLinha()
                                            + " Numtom="+painelAux.getTom().getNumTom()+" Data="+painelAux.getTom().getData());
                                    t1.setVisible(true);
                                    t1.setLocationRelativeTo(null);
                                

                            }

                            @Override
                            public void mouseEntered(java.awt.event.MouseEvent evt) {
                                //evento passar mouse aqui

                                painelAux.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

                                //matrizpainel[x][y].setBackground(Color.red);
                            }

                            @Override
                            public void mouseExited(java.awt.event.MouseEvent evt) {
                                //evento sair do target aqui
                                painelAux.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                            }

                        });
                        //final evento

                        //escolher cor de acordo com o estado
                       /*  String sql2 = "select i.estado from tomate t, imagem_processada i where \n"
                         + "t.rua=i.Tomate_rua and t.linha=i.Tomate_linha and t.numtom=i.Tomate_numtom and t.data=i.Tomate_data and \n"
                         + "t.linha='" + rs.getString("linha") + "' and t.rua=" + rs.getString("Rua") + " and t.numtom=" + rs.getString("numtom") + " and t.data=" + rs.getString("data") + ";";
                         Statement stmt2 = con.createStatement();
                         ResultSet rs2 = stmt2.executeQuery(sql2);
                         rs2.next();*/
                        int estado = Integer.parseInt(rs.getString("estado"));
                        switch (estado) {
                            case 1: {
                                matrizpainel[x][y].setBackground(new Color(0, 128, 0));
                                break;
                            }//Green
                            case 2: {
                                matrizpainel[x][y].setBackground(new Color(144, 238, 144));
                                break;
                            }//LightGreen
                            case 3: {
                                matrizpainel[x][y].setBackground(new Color(255, 255, 0));
                                break;
                            }//Yellow
                            case 4: {
                                matrizpainel[x][y].setBackground(new Color(255, 165, 0));
                                break;
                            }//Orange
                            case 5: {
                                matrizpainel[x][y].setBackground(new Color(255, 140, 0));
                                break;
                            }//DarkOrange
                            case 6: {
                                matrizpainel[x][y].setBackground(new Color(255, 69, 0));
                                break;
                            }//OrangeRed
                            default: {
                                matrizpainel[x][y].setBackground(new Color(0, 128, 0));
                                break;
                            }//Green
                        }
                    }

                    add(matrizpainel[x][y]);
                }
            }
                 con.close();
        } catch (SQLException e) {
            System.out.println("Erro no SQL2:" + e.getMessage());
        }

      
    }

    public boolean isInt(String v) {
        try {
            Integer.parseInt(v);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
