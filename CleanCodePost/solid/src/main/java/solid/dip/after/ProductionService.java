package solid.dip.after;

import solid.dip.Production;

public class ProductionService {

    private final Validator validator;

    public ProductionService(Validator validator) {
        this.validator = validator;
    }

    public void validate(Production production) {
        validator.validate(production);
    }
}
