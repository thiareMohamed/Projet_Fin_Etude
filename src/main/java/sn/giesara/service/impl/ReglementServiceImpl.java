package sn.giesara.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.giesara.domain.Reglement;
import sn.giesara.repository.ReglementRepository;
import sn.giesara.service.ReglementService;

/**
 * Service Implementation for managing {@link Reglement}.
 */
@Service
@Transactional
public class ReglementServiceImpl implements ReglementService {

    private final Logger log = LoggerFactory.getLogger(ReglementServiceImpl.class);

    private final ReglementRepository reglementRepository;

    public ReglementServiceImpl(ReglementRepository reglementRepository) {
        this.reglementRepository = reglementRepository;
    }

    @Override
    public Reglement save(Reglement reglement) {
        log.debug("Request to save Reglement : {}", reglement);
        return reglementRepository.save(reglement);
    }

    @Override
    public Reglement update(Reglement reglement) {
        log.debug("Request to save Reglement : {}", reglement);
        return reglementRepository.save(reglement);
    }

    @Override
    public Optional<Reglement> partialUpdate(Reglement reglement) {
        log.debug("Request to partially update Reglement : {}", reglement);

        return reglementRepository
            .findById(reglement.getId())
            .map(existingReglement -> {
                if (reglement.getSommeVerse() != null) {
                    existingReglement.setSommeVerse(reglement.getSommeVerse());
                }
                if (reglement.getDateReglement() != null) {
                    existingReglement.setDateReglement(reglement.getDateReglement());
                }

                return existingReglement;
            })
            .map(reglementRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reglement> findAll() {
        log.debug("Request to get all Reglements");
        return reglementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reglement> findOne(Long id) {
        log.debug("Request to get Reglement : {}", id);
        return reglementRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reglement : {}", id);
        reglementRepository.deleteById(id);
    }
}
