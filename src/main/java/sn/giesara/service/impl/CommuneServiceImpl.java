package sn.giesara.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.giesara.domain.Commune;
import sn.giesara.repository.CommuneRepository;
import sn.giesara.service.CommuneService;

/**
 * Service Implementation for managing {@link Commune}.
 */
@Service
@Transactional
public class CommuneServiceImpl implements CommuneService {

    private final Logger log = LoggerFactory.getLogger(CommuneServiceImpl.class);

    private final CommuneRepository communeRepository;

    public CommuneServiceImpl(CommuneRepository communeRepository) {
        this.communeRepository = communeRepository;
    }

    @Override
    public Commune save(Commune commune) {
        log.debug("Request to save Commune : {}", commune);
        return communeRepository.save(commune);
    }

    @Override
    public Commune update(Commune commune) {
        log.debug("Request to save Commune : {}", commune);
        return communeRepository.save(commune);
    }

    @Override
    public Optional<Commune> partialUpdate(Commune commune) {
        log.debug("Request to partially update Commune : {}", commune);

        return communeRepository
            .findById(commune.getId())
            .map(existingCommune -> {
                if (commune.getNom() != null) {
                    existingCommune.setNom(commune.getNom());
                }

                return existingCommune;
            })
            .map(communeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Commune> findAll() {
        log.debug("Request to get all Communes");
        return communeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Commune> findOne(Long id) {
        log.debug("Request to get Commune : {}", id);
        return communeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Commune : {}", id);
        communeRepository.deleteById(id);
    }
}
