package solid.lsp.after;

import org.junit.jupiter.api.Test;
import solid.lsp.Production;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AfterProductionServiceTest {

    @Test
    void addProduction() {
        ProductionService afterProductionService = new ProductionService();

        Production preProduction = new Production("pre");

        List<Production> productions = afterProductionService.addProduction(preProduction);

        Production postProduction = new Production("post");

        assertAll(
                () -> assertThat(productions.size()).isEqualTo(2),
                () -> productions.add(postProduction)
        );
    }
}