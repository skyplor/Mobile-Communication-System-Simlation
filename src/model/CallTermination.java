/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Sky
 */
public class CallTermination extends Event
{

    private BaseStation at;

    public CallTermination(Car car, BaseStation at)
    {
        super(car);
        this.at = at;
    }

    public BaseStation getAt()
    {
        return at;
    }

    public void setAt(BaseStation at)
    {
        this.at = at;
    }
}
