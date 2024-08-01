package framework.custom.models.robot;

import com.google.gson.annotations.SerializedName;

public class ArmStep {
    @SerializedName("Order")
    private int order;

    @SerializedName("Base")
    private int base;

    @SerializedName("Shoulder")
    private int shoulder;

    @SerializedName("Elbow")
    private int elbow;

    @SerializedName("Hand")
    private int hand;

    @SerializedName("Wrist")
    private int wrist;

    @SerializedName("Grip")
    private int grip;

    @SerializedName("Speed")
    private int speed;

    @SerializedName("Pause")
    private int pause;

    public ArmStep() { }
    public ArmStep(ArmStep step)
    {
        copyStep(step);
    }

    public void copyStep(ArmStep step)
    {
        this.order = step.order;
        this.base = step.base;
        this.shoulder = step.shoulder;
        this.elbow = step.elbow;
        this.hand = step.hand;
        this.wrist = step.wrist;
        this.grip = step.grip;
        this.speed = step.speed;
        this.pause = step.pause;
    }
    public String stringToSend()
    {
        return "ARM," + "V" + speed + ",B" + base + ",S" + shoulder + ",E" + elbow + ",H" + hand + ",W" + wrist + ",G" + grip + ",P" + pause;
    }
    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }
    public int getBase() {
        return base;
    }
    public void setBase(int base) {
        this.base = base;
    }
    public int getShoulder() {
        return shoulder;
    }
    public void setShoulder(int shoulder) {
        this.shoulder = shoulder;
    }
    public int getElbow() {
        return elbow;
    }
    public void setElbow(int elbow) {
        this.elbow = elbow;
    }
    public int getHand() {
        return hand;
    }
    public void setHand(int hand) {
        this.hand = hand;
    }
    public int getWrist() {
        return wrist;
    }
    public void setWrist(int wrist) {
        this.wrist = wrist;
    }
    public int getGrip() {
        return grip;
    }
    public void setGrip(int grip) {
        this.grip = grip;
    }
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getPause() {
        return pause;
    }
    public void setPause(int pause) {
        this.pause = pause;
    }
}
