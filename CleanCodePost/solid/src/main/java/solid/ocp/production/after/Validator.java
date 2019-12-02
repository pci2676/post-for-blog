package solid.ocp.production.after;

import solid.ocp.production.Production;

public interface Validator {

    boolean support(Production production);

    void validate(Production production) throws IllegalArgumentException;

}
