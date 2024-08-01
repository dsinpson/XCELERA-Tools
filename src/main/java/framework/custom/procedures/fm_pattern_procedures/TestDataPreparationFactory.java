package framework.custom.procedures.fm_pattern_procedures;

import framework.custom.procedures.TestDataPreparation;
import framework.data.entities.Procedure;
import framework.helpers.ScreenshotHelper;

public class TestDataPreparationFactory extends ProcedureBase{

    private final ScreenshotHelper screenshotHelper;
    private  final Procedure procedure;

    public TestDataPreparationFactory(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.screenshotHelper=screenshotHelper;
        this.procedure=procedure;
    }

    @Override
    public IPerformableProcedure createProcedure() {
        return new TestDataPreparation(procedure,screenshotHelper);
    }
}
