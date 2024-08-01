package framework.custom.tasks.iseries.myextra;

import framework.custom.tasks.ITask;

public class RestartGaen implements ITask {

    public static RestartGaen task(){
        return new RestartGaen();
    }
    @Override
    public void perform() {

    }
}
