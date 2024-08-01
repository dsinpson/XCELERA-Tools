package framework.custom.procedures.fm_pattern_procedures;

import framework.custom.procedures.VerificarTransaccionEnTira;
import framework.data.entities.Procedure;
import framework.helpers.ScreenshotHelper;

public class VerificarTransaccionEnTiraFactory extends ProcedureBase {
    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    public VerificarTransaccionEnTiraFactory(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.procedure=procedure;
        this.screenshotHelper=screenshotHelper;
    }

    @Override
    public IPerformableProcedure createProcedure() {
        return new VerificarTransaccionEnTira(procedure,screenshotHelper);
    }
}
