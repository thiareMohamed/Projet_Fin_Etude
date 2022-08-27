package sn.giesara.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.giesara.domain.Village;
import sn.giesara.repository.VillageRepository;
import sn.giesara.service.VillageService;

/**
 * Service Implementation for managing {@link Village}.
 */
@Service
@Transactional
public class VillageServiceImpl implements VillageService {

    private final Logger log = LoggerFactory.getLogger(VillageServiceImpl.class);

    private final VillageRepository villageRepository;

    public VillageServiceImpl(VillageRepository villageRepository) {
        this.villageRepository = villageRepository;
    }

    @Override
    public Village save(Village village) {
        log.debug("Request to save Village : {}", village);
        return villageRepository.save(village);
    }

    @Override
    public Village update(Village village) {
        log.debug("Request to save Village : {}", village);
        return villageRepository.save(village);
    }

    @Override
    public Optional<Village> partialUpdate(Village village) {
        log.debug("Request to partially update Village : {}", village);

        return villageRepository
            .findById(village.getId())
            .map(existingVillage -> {
                if (village.getNom() != null) {
                    existingVillage.setNom(village.getNom());
                }
                if (village.getPrenomNomChefVillage() != null) {
                    existingVillage.setPrenomNomChefVillage(village.getPrenomNomChefVillage());
                }

                return existingVillage;
            })
            .map(villageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Village> findAll() {
        log.debug("Request to get all Villages");
        return villageRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Village> findOne(Long id) {
        log.debug("Request to get Village : {}", id);
        return villageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Village : {}", id);
        villageRepository.deleteById(id);
    }
}
