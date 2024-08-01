package framework.custom.procedures.fm_pattern_procedures;

import framework.custom.procedures.IseriesBalanceValidation;
import framework.data.entities.Procedure;
import framework.helpers.ScreenshotHelper;

public class BalanceIseriesFactory extends ProcedureBase {

    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;
    public BalanceIseriesFactory(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.procedure = procedure;
        this.screenshotHelper = screenshotHelper;
    }

    @Override
    public IPerformableProcedure createProcedure() throws Exception {
        return new IseriesBalanceValidation(procedure,screenshotHelper);
    }
}
