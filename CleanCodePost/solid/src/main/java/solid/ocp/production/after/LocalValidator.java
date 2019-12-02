package solid.ocp.production.after;

import solid.ocp.production.Production;

public class LocalValidator implements Validator {
    @Override
    public boolean support(Production production) {
        return production.getOption().equals("L");
    }

    @Override
    public void validate(Production production) throws IllegalArgumentException {
        if (production.getNameLength() < 20) {
            throw new IllegalArgumentException("지역 상품의 이름은 20글자보다 길어야 합니다.");
        }
    }
}
