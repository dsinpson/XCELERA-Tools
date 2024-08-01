package framework.custom.tasks.iseries.myextra;

import framework.custom.tasks.ITask;

public class RestartAT7Server implements ITask {
    public static RestartAT7Server task(){
        return new RestartAT7Server();
    }
    @Override
    public void perform() {

    }
}
