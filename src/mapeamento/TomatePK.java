/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeamento;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Gus
 */
@Embeddable
public class TomatePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "numtom")
    private String numtom;
    @Basic(optional = false)
    @Column(name = "rua")
    private String rua;
    @Basic(optional = false)
    @Column(name = "linha")
    private String linha;
    @Basic(optional = false)
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;

    public TomatePK() {
    }

    public TomatePK(String numtom, String rua, String linha, Date data) {
        this.numtom = numtom;
        this.rua = rua;
        this.linha = linha;
        this.data = data;
    }

    public String getNumtom() {
        return numtom;
    }

    public void setNumtom(String numtom) {
        this.numtom = numtom;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numtom != null ? numtom.hashCode() : 0);
        hash += (rua != null ? rua.hashCode() : 0);
        hash += (linha != null ? linha.hashCode() : 0);
        hash += (data != null ? data.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TomatePK)) {
            return false;
        }
        TomatePK other = (TomatePK) object;
        if ((this.numtom == null && other.numtom != null) || (this.numtom != null && !this.numtom.equals(other.numtom))) {
            return false;
        }
        if ((this.rua == null && other.rua != null) || (this.rua != null && !this.rua.equals(other.rua))) {
            return false;
        }
        if ((this.linha == null && other.linha != null) || (this.linha != null && !this.linha.equals(other.linha))) {
            return false;
        }
        if ((this.data == null && other.data != null) || (this.data != null && !this.data.equals(other.data))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mapeamento.TomatePK[ numtom=" + numtom + ", rua=" + rua + ", linha=" + linha + ", data=" + data + " ]";
    }
    
}
