package framework.custom.procedures.fm_pattern_procedures;

import framework.custom.procedures.AuthenticValidation;
import framework.data.entities.Procedure;
import framework.helpers.ScreenshotHelper;

public class AuthenticValidationFactory extends ProcedureBase {
    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    public AuthenticValidationFactory(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.procedure=procedure;
        this.screenshotHelper=screenshotHelper;
    }

    @Override
    public IPerformableProcedure createProcedure() {
        return new AuthenticValidation(procedure,screenshotHelper);
    }
}
