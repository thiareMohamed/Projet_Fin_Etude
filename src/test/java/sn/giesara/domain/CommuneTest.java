package sn.giesara.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.giesara.web.rest.TestUtil;

class CommuneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commune.class);
        Commune commune1 = new Commune();
        commune1.setId(1L);
        Commune commune2 = new Commune();
        commune2.setId(commune1.getId());
        assertThat(commune1).isEqualTo(commune2);
        commune2.setId(2L);
        assertThat(commune1).isNotEqualTo(commune2);
        commune1.setId(null);
        assertThat(commune1).isNotEqualTo(commune2);
    }
}
