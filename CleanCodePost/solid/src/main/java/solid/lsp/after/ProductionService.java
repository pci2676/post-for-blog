package solid.lsp.after;

import solid.lsp.Production;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductionService {


    public List<Production> addProduction(Production production) {

        Production before = new Production("before");

        return new ArrayList<>(Arrays.asList(production, before));
    }
}
