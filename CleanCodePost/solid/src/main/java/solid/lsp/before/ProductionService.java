package solid.lsp.before;

import solid.lsp.Production;

import java.util.Arrays;
import java.util.List;

public class ProductionService {

    public List<Production> addProduction(Production production) {

        Production before = new Production("before");

        return Arrays.asList(production, before);
    }
}
