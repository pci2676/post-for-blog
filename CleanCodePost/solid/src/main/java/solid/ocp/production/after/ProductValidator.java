package solid.ocp.production.after;

import solid.ocp.production.Production;

import java.util.Arrays;
import java.util.List;

public class ProductValidator {

    private final List<Validator> validators = Arrays.asList(new DefaultValidator(), new ETicketValidator(), new LocalValidator());

    public void validate(Production production) {
        Validator productionValidator = new DefaultValidator();

        for (Validator validator : validators) {
            if (validator.support(production)) {
                productionValidator = validator;
                break;
            }
        }

        productionValidator.validate(production);
    }
}
