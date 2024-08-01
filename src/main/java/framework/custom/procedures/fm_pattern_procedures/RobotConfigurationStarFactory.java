package framework.custom.procedures.fm_pattern_procedures;

import framework.custom.procedures.RobotConfigurationStar;

public class RobotConfigurationStarFactory extends ProcedureBase{
    @Override
    public IPerformableProcedure createProcedure() {
        return new RobotConfigurationStar();
    }
}
