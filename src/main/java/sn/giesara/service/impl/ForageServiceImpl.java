package sn.giesara.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.giesara.domain.Forage;
import sn.giesara.repository.ForageRepository;
import sn.giesara.service.ForageService;

/**
 * Service Implementation for managing {@link Forage}.
 */
@Service
@Transactional
public class ForageServiceImpl implements ForageService {

    private final Logger log = LoggerFactory.getLogger(ForageServiceImpl.class);

    private final ForageRepository forageRepository;

    public ForageServiceImpl(ForageRepository forageRepository) {
        this.forageRepository = forageRepository;
    }

    @Override
    public Forage save(Forage forage) {
        log.debug("Request to save Forage : {}", forage);
        return forageRepository.save(forage);
    }

    @Override
    public Forage update(Forage forage) {
        log.debug("Request to save Forage : {}", forage);
        return forageRepository.save(forage);
    }

    @Override
    public Optional<Forage> partialUpdate(Forage forage) {
        log.debug("Request to partially update Forage : {}", forage);

        return forageRepository
            .findById(forage.getId())
            .map(existingForage -> {
                if (forage.getNomSite() != null) {
                    existingForage.setNomSite(forage.getNomSite());
                }
                if (forage.getLongitude() != null) {
                    existingForage.setLongitude(forage.getLongitude());
                }
                if (forage.getLatitude() != null) {
                    existingForage.setLatitude(forage.getLatitude());
                }
                if (forage.getDateInstalation() != null) {
                    existingForage.setDateInstalation(forage.getDateInstalation());
                }
                if (forage.getProfondeurForage() != null) {
                    existingForage.setProfondeurForage(forage.getProfondeurForage());
                }
                if (forage.getHauteur() != null) {
                    existingForage.setHauteur(forage.getHauteur());
                }
                if (forage.getCapacite() != null) {
                    existingForage.setCapacite(forage.getCapacite());
                }
                if (forage.getHauteurSousRadier() != null) {
                    existingForage.setHauteurSousRadier(forage.getHauteurSousRadier());
                }
                if (forage.getTypeParatonnerre() != null) {
                    existingForage.setTypeParatonnerre(forage.getTypeParatonnerre());
                }
                if (forage.getTypeReservoir() != null) {
                    existingForage.setTypeReservoir(forage.getTypeReservoir());
                }
                if (forage.getCapaciteReservoir() != null) {
                    existingForage.setCapaciteReservoir(forage.getCapaciteReservoir());
                }

                return existingForage;
            })
            .map(forageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Forage> findAll() {
        log.debug("Request to get all Forages");
        return forageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Forage> findOne(Long id) {
        log.debug("Request to get Forage : {}", id);
        return forageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Forage : {}", id);
        forageRepository.deleteById(id);
    }
}
