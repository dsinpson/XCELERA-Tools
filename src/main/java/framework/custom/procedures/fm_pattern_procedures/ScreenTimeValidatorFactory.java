package framework.custom.procedures.fm_pattern_procedures;

import framework.custom.procedures.ScreenTimeValidator;
import framework.data.entities.Procedure;
import framework.helpers.ScreenshotHelper;

public class ScreenTimeValidatorFactory extends ProcedureBase{

    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    public ScreenTimeValidatorFactory(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.procedure=procedure;
        this.screenshotHelper=screenshotHelper;
    }

    @Override
    public IPerformableProcedure createProcedure() {
        return new ScreenTimeValidator(procedure,screenshotHelper);
    }
}
