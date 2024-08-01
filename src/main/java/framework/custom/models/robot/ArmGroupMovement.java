package framework.custom.models.robot;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArmGroupMovement {
    @SerializedName("Name")
    private String name;

    @SerializedName("StepsCube")
    private ArrayList<String> stepsCube = new ArrayList<String>();

    public ArrayList<String> getStepCube(){
        return stepsCube;
    }

    public void setStepCube(ArrayList<String> stepCube) {
        this.stepsCube = stepCube;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
