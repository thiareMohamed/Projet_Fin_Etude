package sn.giesara.service.impl;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.giesara.domain.*;
import sn.giesara.repository.*;
import sn.giesara.service.CompteurService;
import sn.giesara.service.dto.AddCompteurDto;

/**
 * Service Implementation for managing {@link Compteur}.
 */
@Service
@Transactional
public class CompteurServiceImpl implements CompteurService {

    private final Logger log = LoggerFactory.getLogger(CompteurServiceImpl.class);

    private final CompteurRepository compteurRepository;
    private VillageRepository villageRepository;
    private ClientRepository clientRepository;
    private AbonnementRepository abonnementRepository;
    private ForageRepository forageRepository;

    public CompteurServiceImpl(CompteurRepository compteurRepository,
                               VillageRepository villageRepository,
                               ClientRepository clientRepository,
                               AbonnementRepository abonnementRepository,
                               ForageRepository forageRepository) {
        this.compteurRepository = compteurRepository;
        this.villageRepository = villageRepository;
        this.clientRepository = clientRepository;
        this.abonnementRepository = abonnementRepository;
        this.forageRepository = forageRepository;
    }

    @Override
    public Compteur save(AddCompteurDto compteur) {
        log.debug("Request to save Compteur : {}", compteur);
        Compteur compteur1 = new Compteur();
        compteur1.setNumeroCompteur(generateUniqueId());
        compteur1.setTypeCompteur(compteur.getTypeCompteur());
        compteur1.setMarque(compteur.getMarque());
        compteur1.setDateAbonnement(compteur.getDateAbonnement());
        compteur1.setStatut(compteur.isStatus());

        Village village = villageRepository.findById(compteur.getIdVillage()).orElse(null);
        Client client = clientRepository.findById(compteur.getIdClient()).orElse(null);
        Abonnement abonnement = abonnementRepository.findById(compteur.getIdAbonnement()).orElse(null);
        Forage forage = forageRepository.findById(compteur.getIdForage()).orElse(null);

            compteur1.setVillage(village);
            compteur1.setClient(client);
            compteur1.setAbonnement(abonnement);
            compteur1.setForage(forage);

        return compteurRepository.save(compteur1);
    }

    private static Long generateUniqueId() {
        Long val = -1L;
        final UUID uid = UUID.randomUUID();
        final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uid.getLeastSignificantBits());
        buffer.putLong(uid.getMostSignificantBits());
        final BigInteger bi = new BigInteger(buffer.array());
        val = bi.longValue() & Long.MAX_VALUE;
        return val;
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
