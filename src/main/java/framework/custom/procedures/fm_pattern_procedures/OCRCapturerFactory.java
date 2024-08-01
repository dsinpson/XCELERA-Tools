package framework.custom.procedures.fm_pattern_procedures;

import framework.custom.procedures.OCRCapturer;
import framework.data.entities.ExecutionData;
import framework.data.entities.Procedure;
import framework.helpers.ScreenshotHelper;
import framework.testtools.ITestToolFunctions;

public class OCRCapturerFactory extends ProcedureBase{

    private final ExecutionData testToExecute;
    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;
    private final ITestToolFunctions testToolFunctions;

    public OCRCapturerFactory(ExecutionData testToExecute,
                              Procedure procedure,
                              ScreenshotHelper screenshotHelper,
                              ITestToolFunctions testToolFunctions) {
        this.testToExecute = testToExecute;
        this.procedure = procedure;
        this.screenshotHelper=screenshotHelper;
        this.testToolFunctions=testToolFunctions;
    }

    @Override
    public IPerformableProcedure createProcedure() {
        return new OCRCapturer(testToExecute,procedure,screenshotHelper);
    }
}
