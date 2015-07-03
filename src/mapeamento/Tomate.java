/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeamento;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gus
 */
@Entity
@Table(name = "tomate")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tomate.findAll", query = "SELECT t FROM Tomate t"),
    @NamedQuery(name = "Tomate.findByNomearquivo", query = "SELECT t FROM Tomate t WHERE t.nomearquivo = :nomearquivo"),
    @NamedQuery(name = "Tomate.findByNumtom", query = "SELECT t FROM Tomate t WHERE t.tomatePK.numtom = :numtom"),
    @NamedQuery(name = "Tomate.findByRua", query = "SELECT t FROM Tomate t WHERE t.tomatePK.rua = :rua"),
    @NamedQuery(name = "Tomate.findByLinha", query = "SELECT t FROM Tomate t WHERE t.tomatePK.linha = :linha"),
    @NamedQuery(name = "Tomate.findByLat", query = "SELECT t FROM Tomate t WHERE t.lat = :lat"),
    @NamedQuery(name = "Tomate.findByLongi", query = "SELECT t FROM Tomate t WHERE t.longi = :longi"),
    @NamedQuery(name = "Tomate.findByData", query = "SELECT t FROM Tomate t WHERE t.tomatePK.data = :data")})
public class Tomate implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TomatePK tomatePK;
    @Basic(optional = false)
    @Column(name = "nomearquivo")
    private String nomearquivo;
    @Column(name = "lat")
    private String lat;
    @Column(name = "longi")
    private String longi;

    public Tomate() {
    }

    public Tomate(TomatePK tomatePK) {
        this.tomatePK = tomatePK;
    }

    public Tomate(TomatePK tomatePK, String nomearquivo) {
        this.tomatePK = tomatePK;
        this.nomearquivo = nomearquivo;
    }

    public Tomate(String numtom, String rua, String linha, Date data) {
        this.tomatePK = new TomatePK(numtom, rua, linha, data);
    }

    public TomatePK getTomatePK() {
        return tomatePK;
    }

    public void setTomatePK(TomatePK tomatePK) {
        this.tomatePK = tomatePK;
    }

    public String getNomearquivo() {
        return nomearquivo;
    }

    public void setNomearquivo(String nomearquivo) {
        String oldNomearquivo = this.nomearquivo;
        this.nomearquivo = nomearquivo;
        changeSupport.firePropertyChange("nomearquivo", oldNomearquivo, nomearquivo);
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        String oldLat = this.lat;
        this.lat = lat;
        changeSupport.firePropertyChange("lat", oldLat, lat);
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        String oldLongi = this.longi;
        this.longi = longi;
        changeSupport.firePropertyChange("longi", oldLongi, longi);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tomatePK != null ? tomatePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tomate)) {
            return false;
        }
        Tomate other = (Tomate) object;
        if ((this.tomatePK == null && other.tomatePK != null) || (this.tomatePK != null && !this.tomatePK.equals(other.tomatePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mapeamento.Tomate[ tomatePK=" + tomatePK + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
