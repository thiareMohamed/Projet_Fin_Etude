package sn.giesara.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.giesara.domain.Personne;
import sn.giesara.repository.PersonneRepository;
import sn.giesara.service.PersonneService;

/**
 * Service Implementation for managing {@link Personne}.
 */
@Service
@Transactional
public class PersonneServiceImpl implements PersonneService {

    private final Logger log = LoggerFactory.getLogger(PersonneServiceImpl.class);

    private final PersonneRepository personneRepository;

    public PersonneServiceImpl(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    @Override
    public Personne save(Personne personne) {
        log.debug("Request to save Personne : {}", personne);
        return personneRepository.save(personne);
    }

    @Override
    public Personne update(Personne personne) {
        log.debug("Request to save Personne : {}", personne);
        return personneRepository.save(personne);
    }

    @Override
    public Optional<Personne> partialUpdate(Personne personne) {
        log.debug("Request to partially update Personne : {}", personne);

        return personneRepository
            .findById(personne.getId())
            .map(existingPersonne -> {
                if (personne.getNom() != null) {
                    existingPersonne.setNom(personne.getNom());
                }
                if (personne.getPrenom() != null) {
                    existingPersonne.setPrenom(personne.getPrenom());
                }
                if (personne.getDateNaissance() != null) {
                    existingPersonne.setDateNaissance(personne.getDateNaissance());
                }
                if (personne.getLieuNaissance() != null) {
                    existingPersonne.setLieuNaissance(personne.getLieuNaissance());
                }
                if (personne.getSexe() != null) {
                    existingPersonne.setSexe(personne.getSexe());
                }
                if (personne.getNumeroTelephone() != null) {
                    existingPersonne.setNumeroTelephone(personne.getNumeroTelephone());
                }
                if (personne.getNumeroCni() != null) {
                    existingPersonne.setNumeroCni(personne.getNumeroCni());
                }

                return existingPersonne;
            })
            .map(personneRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Personne> findAll() {
        log.debug("Request to get all Personnes");
        return personneRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Personne> findOne(Long id) {
        log.debug("Request to get Personne : {}", id);
        return personneRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Personne : {}", id);
        personneRepository.deleteById(id);
    }
}
