package framework.custom.procedures.fm_pattern_procedures;

import framework.custom.procedures.AuthenticEnvironmentValidation;
import framework.data.entities.Procedure;
import framework.helpers.ScreenshotHelper;

public class AuthenticEnvironmentValidationFactory extends ProcedureBase{
    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    public AuthenticEnvironmentValidationFactory(Procedure procedure, ScreenshotHelper _screenshotHelper){
        this.procedure=procedure;
        this.screenshotHelper=_screenshotHelper;
    }

    @Override
    public IPerformableProcedure createProcedure() {
        return new AuthenticEnvironmentValidation(procedure,screenshotHelper);
    }
}
