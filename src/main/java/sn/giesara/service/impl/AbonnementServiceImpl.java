package sn.giesara.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.giesara.domain.Abonnement;
import sn.giesara.repository.AbonnementRepository;
import sn.giesara.service.AbonnementService;

/**
 * Service Implementation for managing {@link Abonnement}.
 */
@Service
@Transactional
public class AbonnementServiceImpl implements AbonnementService {

    private final Logger log = LoggerFactory.getLogger(AbonnementServiceImpl.class);

    private final AbonnementRepository abonnementRepository;

    public AbonnementServiceImpl(AbonnementRepository abonnementRepository) {
        this.abonnementRepository = abonnementRepository;
    }

    @Override
    public Abonnement save(Abonnement abonnement) {
        log.debug("Request to save Abonnement : {}", abonnement);
        return abonnementRepository.save(abonnement);
    }

    @Override
    public Abonnement update(Abonnement abonnement) {
        log.debug("Request to save Abonnement : {}", abonnement);
        return abonnementRepository.save(abonnement);
    }

    @Override
    public Optional<Abonnement> partialUpdate(Abonnement abonnement) {
        log.debug("Request to partially update Abonnement : {}", abonnement);

        return abonnementRepository
            .findById(abonnement.getId())
            .map(existingAbonnement -> {
                if (abonnement.getLibelle() != null) {
                    existingAbonnement.setLibelle(abonnement.getLibelle());
                }
                if (abonnement.getPrixUnitaire() != null) {
                    existingAbonnement.setPrixUnitaire(abonnement.getPrixUnitaire());
                }

                return existingAbonnement;
            })
            .map(abonnementRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Abonnement> findAll() {
        log.debug("Request to get all Abonnements");
        return abonnementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Abonnement> findOne(Long id) {
        log.debug("Request to get Abonnement : {}", id);
        return abonnementRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Abonnement : {}", id);
        abonnementRepository.deleteById(id);
    }
}
