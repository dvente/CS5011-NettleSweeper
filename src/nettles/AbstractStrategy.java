package nettles;

// simpy for things to extend to use in the factory
public abstract class AbstractStrategy {

    @Override
    public String toString() {

        return this.getClass().getName();
    }

}
