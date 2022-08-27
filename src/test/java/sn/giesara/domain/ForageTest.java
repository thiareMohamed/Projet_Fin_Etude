package sn.giesara.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.giesara.web.rest.TestUtil;

class ForageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Forage.class);
        Forage forage1 = new Forage();
        forage1.setId(1L);
        Forage forage2 = new Forage();
        forage2.setId(forage1.getId());
        assertThat(forage1).isEqualTo(forage2);
        forage2.setId(2L);
        assertThat(forage1).isNotEqualTo(forage2);
        forage1.setId(null);
        assertThat(forage1).isNotEqualTo(forage2);
    }
}
