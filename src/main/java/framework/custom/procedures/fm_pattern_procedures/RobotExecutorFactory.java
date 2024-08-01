package framework.custom.procedures.fm_pattern_procedures;

import framework.custom.procedures.RobotExecutor;
import framework.data.dynamicValues.DynamicValuesHelper;
import framework.data.entities.Procedure;
import framework.helpers.ScreenshotHelper;

public class RobotExecutorFactory extends ProcedureBase{

    DynamicValuesHelper dvh = new DynamicValuesHelper();

    private final Procedure procedure;
    private final ScreenshotHelper screenshotHelper;

    public RobotExecutorFactory(Procedure procedure, ScreenshotHelper screenshotHelper) {
        this.procedure=procedure;
        this.screenshotHelper=screenshotHelper;
    }

    @Override
    public IPerformableProcedure createProcedure() throws Exception {
        try {
            return new RobotExecutor(
                    dvh.captureProp(procedure.Value,"NombreMovimiento",true),
                    dvh.captureProp(procedure.Value,"Tipo",true)
            );
        }catch (Exception e){
            screenshotHelper.takeScreenshot();
            return null;
        }
    }
}
