package framework.custom.procedures.fm_pattern_procedures;

import framework.custom.procedures.CardConfiguration;
import framework.data.entities.Procedure;
import framework.helpers.ScreenshotHelper;

public class CardConfigurationFactory extends ProcedureBase{

    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    public CardConfigurationFactory(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.procedure=procedure;
        this.screenshotHelper=screenshotHelper;
    }
    @Override
    public IPerformableProcedure createProcedure() {
        return new CardConfiguration(procedure,screenshotHelper);
    }
}
