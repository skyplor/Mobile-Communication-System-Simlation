/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Sky
 */
public class DropCall extends Event
{

    private BaseStation at;

    public DropCall(Car car, BaseStation at)
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
