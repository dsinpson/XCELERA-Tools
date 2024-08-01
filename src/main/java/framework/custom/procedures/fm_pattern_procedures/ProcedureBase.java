package framework.custom.procedures.fm_pattern_procedures;


import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ProcedureBase {

    Logger logger=Logger.getLogger(ProcedureBase.class.getName());

    public void executeCustomProcedure() throws Exception {
        try {
            IPerformableProcedure performableProcedure = createProcedure();
            performableProcedure.executeCustomProcedure();
        } catch (Exception e) {
            logger.log(Level.SEVERE,() ->"Exception on procedure - Mensaje: " + e.getMessage());
            throw e;
        }
    }
    public abstract IPerformableProcedure createProcedure() throws Exception;
}
