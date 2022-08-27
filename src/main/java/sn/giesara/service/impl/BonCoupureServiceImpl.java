package sn.giesara.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.giesara.domain.BonCoupure;
import sn.giesara.repository.BonCoupureRepository;
import sn.giesara.service.BonCoupureService;

/**
 * Service Implementation for managing {@link BonCoupure}.
 */
@Service
@Transactional
public class BonCoupureServiceImpl implements BonCoupureService {

    private final Logger log = LoggerFactory.getLogger(BonCoupureServiceImpl.class);

    private final BonCoupureRepository bonCoupureRepository;

    public BonCoupureServiceImpl(BonCoupureRepository bonCoupureRepository) {
        this.bonCoupureRepository = bonCoupureRepository;
    }

    @Override
    public BonCoupure save(BonCoupure bonCoupure) {
        log.debug("Request to save BonCoupure : {}", bonCoupure);
        return bonCoupureRepository.save(bonCoupure);
    }

    @Override
    public BonCoupure update(BonCoupure bonCoupure) {
        log.debug("Request to save BonCoupure : {}", bonCoupure);
        return bonCoupureRepository.save(bonCoupure);
    }

    @Override
    public Optional<BonCoupure> partialUpdate(BonCoupure bonCoupure) {
        log.debug("Request to partially update BonCoupure : {}", bonCoupure);

        return bonCoupureRepository
            .findById(bonCoupure.getId())
            .map(existingBonCoupure -> {
                if (bonCoupure.getCodeBon() != null) {
                    existingBonCoupure.setCodeBon(bonCoupure.getCodeBon());
                }
                if (bonCoupure.getRaisonCoupure() != null) {
                    existingBonCoupure.setRaisonCoupure(bonCoupure.getRaisonCoupure());
                }
                if (bonCoupure.getTaxe() != null) {
                    existingBonCoupure.setTaxe(bonCoupure.getTaxe());
                }

                return existingBonCoupure;
            })
            .map(bonCoupureRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BonCoupure> findAll() {
        log.debug("Request to get all BonCoupures");
        return bonCoupureRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BonCoupure> findOne(Long id) {
        log.debug("Request to get BonCoupure : {}", id);
        return bonCoupureRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BonCoupure : {}", id);
        bonCoupureRepository.deleteById(id);
    }
}
