package framework.custom.tasks.iseries.myextra;

import framework.custom.tasks.ITask;

public class RestartAT9Server implements ITask {
    public static RestartAT9Server task(){
        return new RestartAT9Server();
    }
    @Override
    public void perform() {

    }
}
