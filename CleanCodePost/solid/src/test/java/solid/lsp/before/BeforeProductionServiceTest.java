package solid.lsp.before;

import org.junit.jupiter.api.Test;
import solid.lsp.Production;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BeforeProductionServiceTest {

    @Test
    void addProduction() {
        ProductionService beforeProductionService = new ProductionService();

        Production preProduction = new Production("pre");

        List<Production> productions = beforeProductionService.addProduction(preProduction);

        Production postProduction = new Production("post");

        assertAll(
                () -> assertThat(productions.size()).isEqualTo(2),
                () -> productions.add(postProduction)
        );

    }
}