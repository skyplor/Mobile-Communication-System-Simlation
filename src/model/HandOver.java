/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Sky
 */
public class HandOver extends Event
{

    private BaseStation from;

    public HandOver(Car car, BaseStation from)
    {
        super(car);
        this.from = from;
    }

    public BaseStation getFrom()
    {
        return from;
    }

    public void setFrom(BaseStation from)
    {
        this.from = from;
    }
}
