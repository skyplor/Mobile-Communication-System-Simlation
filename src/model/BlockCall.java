/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Sky
 */
public class BlockCall extends Event
{

    private BaseStation at;

    public BlockCall(Car car, BaseStation at)
    {
        super(car);
        this.at = at;
    }

    public BaseStation getAt()
    {
        return at;
    }

    /**
     * Sets the BaseStation in which this block call occurs
     * 
     * @param at BaseStation
     * 
     */
    public void setAt(BaseStation at)
    {
        this.at = at;
    }
}
