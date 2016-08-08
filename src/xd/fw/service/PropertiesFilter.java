package xd.fw.service;

public abstract class PropertiesFilter<T> {
    T t;
    public PropertiesFilter (T t){
        this.t = t;
    }

    public void execute(Processor p){

    }

}
