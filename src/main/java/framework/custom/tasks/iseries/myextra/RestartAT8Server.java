package framework.custom.tasks.iseries.myextra;

import framework.custom.tasks.ITask;

public class RestartAT8Server implements ITask {
    public static RestartAT8Server task(){
        return new RestartAT8Server();
    }
    @Override
    public void perform() {

    }
}
