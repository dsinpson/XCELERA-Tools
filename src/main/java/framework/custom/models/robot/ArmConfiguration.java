package framework.custom.models.robot;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class ArmConfiguration {
    @SerializedName("Name")
    private String name;

    @SerializedName("CurrentPosition")
    private ArmStep currentPosition;

    @SerializedName("Movements")
    private ArrayList<ArmMovement> movements = new ArrayList<ArmMovement>();

    @SerializedName("groupMovements")
    private ArrayList<ArmGroupMovement> groupMovements = new ArrayList<ArmGroupMovement>();

    public ArmConfiguration()
    {

        this.currentPosition = new ArmStep();
        this.currentPosition.setBase(90);
        this.currentPosition.setShoulder(90);
        this.currentPosition.setElbow(90);
        this.currentPosition.setHand(90);
        this.currentPosition.setWrist(90);
        this.currentPosition.setGrip(50);
        this.currentPosition.setSpeed(20);
        this.currentPosition.setPause(0);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArmStep getCurrentPosition() {
        return currentPosition;
    }
    public void setCurrentPosition(ArmStep currentPosition) {
        this.currentPosition = currentPosition;
    }
    public ArrayList<ArmMovement> getMovements() {
        return movements;
    }
    public void setMovements(ArrayList<ArmMovement> movements) {
        this.movements = movements;
    }
    public ArrayList<ArmGroupMovement> getGroupMovements(){
        return this.groupMovements;
    }
    public void setGroupMovements(ArrayList<ArmGroupMovement> groupMovements) {
        this.groupMovements = groupMovements;
    }
    public HashMap<String,ArmMovement> getMovementsMap()
    {
        HashMap<String, ArmMovement> map = new HashMap<String, ArmMovement>();

        for (ArmMovement m : movements)
        {
            map.put(m.getName(), m);
        }


        return map;
    }

    public HashMap<String, ArmGroupMovement> getGroupMovementsMap()
    {
        HashMap<String, ArmGroupMovement> mapGroup = new HashMap<String, ArmGroupMovement>();

        for (ArmGroupMovement gm : groupMovements )
        {
            mapGroup.put(gm.getName(), gm);
        }

        return mapGroup;
    }
}
