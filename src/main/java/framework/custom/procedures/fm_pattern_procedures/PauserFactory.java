package framework.custom.procedures.fm_pattern_procedures;

import framework.custom.procedures.Pauser;
import framework.data.entities.Procedure;
import framework.helpers.ScreenshotHelper;

public class PauserFactory extends ProcedureBase{

    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    public PauserFactory(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.procedure=procedure;
        this.screenshotHelper=screenshotHelper;
    }

    @Override
    public IPerformableProcedure createProcedure() {
        return new Pauser(procedure,screenshotHelper);
    }
}
