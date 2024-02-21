package cointegration;

import lombok.Data;

@Data
public class RawData {
    private String Goal;
    private String Series;
    private String indicator;
    private double[] Time;
    private double[] Value;

    public RawData(){}

    public RawData(int StartYear,int EndYear,String goal,String series,String indicator,double[] time,double[] value){
        this.Goal=goal;
        this.Series=series;
        this.indicator=indicator;
        this.Time=new double[EndYear-StartYear+1];
        this.Value=new double[EndYear-StartYear+1];
        for(int i=0;i<Time.length;i++){
            this.Time[i]=StartYear+i;
        }
        for(int i=0;i<Value.length;i++){
            Value[i]=LinearInterpolation(StartYear+i,time.length,time,value);
        }
    }
    //分段线性插值(包含内插和外插）
    private double LinearInterpolation(double X0, int n,double x[],double y[]){
        double Y=0;
        double temp=100;
        int Index=0;
        //找出离插值点最近的点
        for(int i=0;i<n;i++){
            if(Math.abs(x[i]-X0) <temp){
                temp=Math.abs(x[i]-X0);
                Index=i;
            }
        }
        if(x[Index]==x[0]){
            Y=((y[Index+1]-y[Index])/(x[Index+1]-x[Index]))*(X0-x[Index])+y[Index];
        }else if(x[Index]==x[n-1]){
            Y=((y[Index]-y[Index-1])/(x[Index]-x[Index-1]))*(X0-x[Index])+y[Index];
        }else{
            if(X0>x[Index]){
                Y=((y[Index+1]-y[Index])/(x[Index+1]-x[Index]))*(X0-x[Index])+y[Index];
            }else {
                Y=((y[Index]-y[Index-1])/(x[Index]-x[Index-1]))*(X0-x[Index])+y[Index];
            }
        }

        return Y;
    }
}
