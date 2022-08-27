package sn.giesara.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.giesara.domain.Compteur;
import sn.giesara.repository.CompteurRepository;
import sn.giesara.service.CompteurService;

/**
 * Service Implementation for managing {@link Compteur}.
 */
@Service
@Transactional
public class CompteurServiceImpl implements CompteurService {

    private final Logger log = LoggerFactory.getLogger(CompteurServiceImpl.class);

    private final CompteurRepository compteurRepository;

    public CompteurServiceImpl(CompteurRepository compteurRepository) {
        this.compteurRepository = compteurRepository;
    }

    @Override
    public Compteur save(Compteur compteur) {
        log.debug("Request to save Compteur : {}", compteur);
        return compteurRepository.save(compteur);
    }

    @Override
    public Compteur update(Compteur compteur) {
        log.debug("Request to save Compteur : {}", compteur);
        return compteurRepository.save(compteur);
    }

    @Override
    public Optional<Compteur> partialUpdate(Compteur compteur) {
        log.debug("Request to partially update Compteur : {}", compteur);

        return compteurRepository
            .findById(compteur.getId())
            .map(existingCompteur -> {
                if (compteur.getNumeroCompteur() != null) {
                    existingCompteur.setNumeroCompteur(compteur.getNumeroCompteur());
                }
                if (compteur.getTypeCompteur() != null) {
                    existingCompteur.setTypeCompteur(compteur.getTypeCompteur());
                }
                if (compteur.getDateAbonnement() != null) {
                    existingCompteur.setDateAbonnement(compteur.getDateAbonnement());
                }
                if (compteur.getMarque() != null) {
                    existingCompteur.setMarque(compteur.getMarque());
                }
                if (compteur.getStatut() != null) {
                    existingCompteur.setStatut(compteur.getStatut());
                }

                return existingCompteur;
            })
            .map(compteurRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Compteur> findAll() {
        log.debug("Request to get all Compteurs");
        return compteurRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Compteur> findOne(Long id) {
        log.debug("Request to get Compteur : {}", id);
        return compteurRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Compteur : {}", id);
        compteurRepository.deleteById(id);
    }
}
