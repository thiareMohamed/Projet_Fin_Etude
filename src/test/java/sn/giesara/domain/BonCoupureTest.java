package sn.giesara.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.giesara.web.rest.TestUtil;

class BonCoupureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BonCoupure.class);
        BonCoupure bonCoupure1 = new BonCoupure();
        bonCoupure1.setId(1L);
        BonCoupure bonCoupure2 = new BonCoupure();
        bonCoupure2.setId(bonCoupure1.getId());
        assertThat(bonCoupure1).isEqualTo(bonCoupure2);
        bonCoupure2.setId(2L);
        assertThat(bonCoupure1).isNotEqualTo(bonCoupure2);
        bonCoupure1.setId(null);
        assertThat(bonCoupure1).isNotEqualTo(bonCoupure2);
    }
}
