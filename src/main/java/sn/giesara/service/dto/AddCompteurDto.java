package sn.giesara.service.dto;

import java.time.LocalDate;

public class AddCompteurDto {
    private Long id;
    private String typeCompteur;
    private LocalDate dateAbonnement;
    private String marque;
    private Boolean status;
    private Long idForage;
    private Long idVillage;
    private Long idAbonnement;
    private Long idClient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeCompteur() {
        return typeCompteur;
    }

    public void setTypeCompteur(String typeCompteur) {
        this.typeCompteur = typeCompteur;
    }

    public LocalDate getDateAbonnement() {
        return dateAbonnement;
    }

    public void setDateAbonnement(LocalDate dateAbonnement) {
        this.dateAbonnement = dateAbonnement;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getIdForage() {
        return idForage;
    }

    public void setIdForage(Long idForage) {
        this.idForage = idForage;
    }

    public Long getIdVillage() {
        return idVillage;
    }

    public void setIdVillage(Long idVillage) {
        this.idVillage = idVillage;
    }

    public Long getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(Long idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public Boolean isStatus() {
        return true;
    }
}
