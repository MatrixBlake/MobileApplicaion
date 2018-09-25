package comp5216.sydney.edu.au.homework3;





public class RunningRecords {
    public double runtime;
    public double distance;
    public double pace;
    public double speed;
    public String rundate;


    public RunningRecords(String d,double t, double dis,double p, double s){
        rundate=d;
        speed=s;
        runtime=t;
        distance=dis;
        pace=p;
    }

    public double getRuntime(){
        return runtime;
    }

    public double getDistance(){
        return distance;
    }

    public double getPace(){
        return pace;
    }

    public double getSpeed(){
        return speed;
    }

    public String getRundate() {
        return rundate;
    }
}
