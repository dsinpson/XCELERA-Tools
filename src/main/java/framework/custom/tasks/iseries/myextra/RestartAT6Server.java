package framework.custom.tasks.iseries.myextra;

import framework.custom.tasks.ITask;

public class RestartAT6Server implements ITask {

    public static RestartAT6Server task(){
        return new RestartAT6Server();
    }
    @Override
    public void perform() {

    }
}
