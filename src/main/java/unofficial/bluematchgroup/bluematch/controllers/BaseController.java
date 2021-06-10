package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.config.Resource;

public abstract class BaseController {
    
    public abstract Resource getFxml();

    public void updateView() {}
    public void refreshScreen() {}
}
