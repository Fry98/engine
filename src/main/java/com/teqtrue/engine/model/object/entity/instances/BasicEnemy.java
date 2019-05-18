package com.teqtrue.engine.model.object.entity.instances;

import com.teqtrue.engine.model.Coordinates;
import com.teqtrue.engine.model.GlobalStore;
import com.teqtrue.engine.model.object.entity.AEntity;
import com.teqtrue.engine.model.object.entity.IEntity;

import java.util.List;

public class BasicEnemy extends AEntity {

    private static final long serialVersionUID = 1L;

    public BasicEnemy(Coordinates coordinates) {
        super(coordinates, 8, 5);
    }

    @Override
    public Runnable update() {
        return new Runnable(){        
            @Override
            public void run() {
                Coordinates pos = getCoordinates();

                // find the player
                IEntity player = findPlayer(GlobalStore.getMap().getEntities());
                if (player != null) {
                    Coordinates playerPos = player.getCoordinates();
                    double dx = playerPos.getX() - pos.getX();
                    double dy = playerPos.getY() - pos.getY();
        
                    // rotate towards the player
                    setOrientation(Math.toDegrees(Math.atan2(dy, dx)));
        
                    // run towards the player
                    if (dx*dx + dy*dy > 4) {
                        pos.alterX(Math.cos(getOrientation(true)) * getSpeed() * 0.02);
                        pos.alterY(Math.sin(getOrientation(true)) * getSpeed() * 0.02);
                    }
                }        
            }
        };
    }

    private IEntity findPlayer(List<IEntity> entities) {
        return entities.stream().filter(Player.class::isInstance).findAny().orElse(null);
    }

}
