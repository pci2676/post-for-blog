package io.javable.beanconfiguration;

import io.javable.beanconfiguration.bean.MyCustomBean;
import io.javable.beanconfiguration.component.MyCustomComponent;
import org.springframework.stereotype.Service;

@Service
public class MyService {
    private final MyCustomBean myCustomBean;
    private final MyCustomComponent myCustomComponent;

    public MyService(final MyCustomBean myCustomBean, final MyCustomComponent myCustomComponent) {
        this.myCustomBean = myCustomBean;
        this.myCustomComponent = myCustomComponent;
    }
}
