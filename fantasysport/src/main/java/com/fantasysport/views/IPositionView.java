package com.fantasysport.views;

import com.fantasysport.models.Position;

import java.util.List;

/**
 * Created by bylynka on 4/24/14.
 */
public interface IPositionView {
    public void setPosition(String position, int index);
    public Position getPosition();
    public void setPositions(List<Position> positions);
    public void setPositionListener(IOnPositionSelectedListener listener);
}
