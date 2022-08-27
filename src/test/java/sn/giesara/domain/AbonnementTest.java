package sn.giesara.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.giesara.web.rest.TestUtil;

class AbonnementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Abonnement.class);
        Abonnement abonnement1 = new Abonnement();
        abonnement1.setId(1L);
        Abonnement abonnement2 = new Abonnement();
        abonnement2.setId(abonnement1.getId());
        assertThat(abonnement1).isEqualTo(abonnement2);
        abonnement2.setId(2L);
        assertThat(abonnement1).isNotEqualTo(abonnement2);
        abonnement1.setId(null);
        assertThat(abonnement1).isNotEqualTo(abonnement2);
    }
}
