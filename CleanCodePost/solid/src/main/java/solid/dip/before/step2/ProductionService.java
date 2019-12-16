package solid.dip.before.step2;

import solid.dip.Production;

public class ProductionService {

    private final LocalValidator localValidator;
    private final ETicketValidator eTicketValidator;

    public ProductionService(LocalValidator localValidator, ETicketValidator eTicketValidator) {
        this.localValidator = localValidator;
        this.eTicketValidator = eTicketValidator;
    }

    public void validate(Production production) {
        if (production.getType().equals("L")) {
            localValidator.validate(production);
        } else if (production.getType().equals("E")) {
            eTicketValidator.validate(production);
        }

    }

}
