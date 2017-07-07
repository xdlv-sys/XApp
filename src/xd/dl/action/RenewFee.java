package xd.dl.action;

public class RenewFee {
    int renewPeriod, renewFee;

    public RenewFee(int renewPeriod, int renewFee) {
        this.renewPeriod = renewPeriod;
        this.renewFee = renewFee;
    }

    public int getRenewFee() {
        return renewFee;
    }

    public int getRenewPeriod() {
        return renewPeriod;
    }
}