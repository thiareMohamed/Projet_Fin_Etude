package sn.giesara.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.giesara.web.rest.TestUtil;

class CompteurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compteur.class);
        Compteur compteur1 = new Compteur();
        compteur1.setId(1L);
        Compteur compteur2 = new Compteur();
        compteur2.setId(compteur1.getId());
        assertThat(compteur1).isEqualTo(compteur2);
        compteur2.setId(2L);
        assertThat(compteur1).isNotEqualTo(compteur2);
        compteur1.setId(null);
        assertThat(compteur1).isNotEqualTo(compteur2);
    }
}
