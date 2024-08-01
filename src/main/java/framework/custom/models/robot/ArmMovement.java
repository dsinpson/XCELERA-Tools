package framework.custom.models.robot;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArmMovement {
    @SerializedName("Name")
    private String name;

    @SerializedName("Steps")
    private ArrayList<ArmStep> steps  = new ArrayList<ArmStep>();

    public ArrayList<ArmStep> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<ArmStep> steps) {
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
