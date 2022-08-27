package sn.giesara.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.giesara.domain.Facture;
import sn.giesara.repository.FactureRepository;
import sn.giesara.service.FactureService;

/**
 * Service Implementation for managing {@link Facture}.
 */
@Service
@Transactional
public class FactureServiceImpl implements FactureService {

    private final Logger log = LoggerFactory.getLogger(FactureServiceImpl.class);

    private final FactureRepository factureRepository;

    public FactureServiceImpl(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    @Override
    public Facture save(Facture facture) {
        log.debug("Request to save Facture : {}", facture);
        return factureRepository.save(facture);
    }

    @Override
    public Facture update(Facture facture) {
        log.debug("Request to save Facture : {}", facture);
        return factureRepository.save(facture);
    }

    @Override
    public Optional<Facture> partialUpdate(Facture facture) {
        log.debug("Request to partially update Facture : {}", facture);

        return factureRepository
            .findById(facture.getId())
            .map(existingFacture -> {
                if (facture.getCode() != null) {
                    existingFacture.setCode(facture.getCode());
                }
                if (facture.getDateDernierReleve() != null) {
                    existingFacture.setDateDernierReleve(facture.getDateDernierReleve());
                }
                if (facture.getAncienIndex() != null) {
                    existingFacture.setAncienIndex(facture.getAncienIndex());
                }
                if (facture.getNouvelIndex() != null) {
                    existingFacture.setNouvelIndex(facture.getNouvelIndex());
                }
                if (facture.getDateLimitePaiment() != null) {
                    existingFacture.setDateLimitePaiment(facture.getDateLimitePaiment());
                }
                if (facture.getStatut() != null) {
                    existingFacture.setStatut(facture.getStatut());
                }

                return existingFacture;
            })
            .map(factureRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Facture> findAll() {
        log.debug("Request to get all Factures");
        return factureRepository.findAllWithEagerRelationships();
    }

    public Page<Facture> findAllWithEagerRelationships(Pageable pageable) {
        return factureRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Facture> findOne(Long id) {
        log.debug("Request to get Facture : {}", id);
        return factureRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Facture : {}", id);
        factureRepository.deleteById(id);
    }
}
