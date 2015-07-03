/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeamento;

import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 *
 * @author Gus
 */
public class MeuJPanel extends JPanel{

    Tomates tom;
    
    public MeuJPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public MeuJPanel(LayoutManager layout) {
        super(layout);
    }

    public MeuJPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public MeuJPanel() {
        super();
    }

    public Tomates getTom() {
        return tom;
    }

    public void setTom(Tomates tom) {
        this.tom = tom;
    }
    
    
}
