package unofficial.bluematchgroup.bluematch.controllers;

import unofficial.bluematchgroup.bluematch.config.Resource;

public interface IController {
    
    public abstract Resource getFxml();

    public abstract void updateView();
    public abstract void refreshScreen();
}
