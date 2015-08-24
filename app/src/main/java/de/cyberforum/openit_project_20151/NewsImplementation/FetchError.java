package de.cyberforum.openit_project_20151.NewsImplementation;

import de.cyberforum.openit_project_20151.NewsInterface.FetchError0;

/**
 * Created by Balzer on 24.07.2015.
 */
public class FetchError implements FetchError0 {
    protected FetchMode mode;
    protected String context, error;
    public FetchError(FetchMode mode, String context, String error) {
        this.mode = mode;
        this.context = context;
        this.error = error;
    }

    public FetchMode getMode() { return FetchMode.FM_TOP; }
    public String getContext() { return context; }
    public String getError() { return error; }
}
