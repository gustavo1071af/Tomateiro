/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeamento;

/**
 *
 * @author gizelle
 */
public class Imagem  {

    private String[] imagens;
    private int gradKey;

    public void setImagens(String[] imgs) {
        imagens = imgs;
    }

    public void setGradKey(double gk) {
        int key = (int)gk;
        int min[] = new int[7];
        
        min[0] = key;
        min[1] = Math.abs(key-3);
        min[2] = Math.abs(key-12);
        min[3] = Math.abs(key-22);
        min[4] = Math.abs(key-40);
        min[5] = Math.abs(key-60);
        min[6] = Math.abs(key-77);
        
        System.out.println("chave = "+key);
        
        gradKey = min[0];
        int minGrad = 0;
        for (int i=0; i<7; i++)
            if (min[i] < min[minGrad])
                minGrad = i;
       
        gradKey = minGrad;  
    }

    public String[] getImagens() {
        return imagens;
    }

    public int getGradKey() {
        return gradKey;
    }

}
