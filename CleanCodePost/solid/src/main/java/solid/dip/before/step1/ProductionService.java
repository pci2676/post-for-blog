package solid.dip.before.step1;

import solid.dip.Production;

public class ProductionService {

    private final LocalValidator localValidator;

    public ProductionService(LocalValidator localValidator) {
        this.localValidator = localValidator;
    }

    public void validate(Production production) {
        localValidator.validate(production);
    }

}
