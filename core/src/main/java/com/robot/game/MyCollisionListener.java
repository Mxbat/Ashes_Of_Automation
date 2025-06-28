package com.robot.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.robot.game.attacks.Attack;
import com.robot.game.attacks.BaseEnemyAttack;
import com.robot.game.attacks.EnemyAttack;
import com.robot.game.attacks.PlayerAttack;
import com.robot.game.attacks.TurretBullet;
import com.robot.game.constants.GameSettings;
import com.robot.game.enemies.Enemy;
import com.robot.game.enemies.BaseEnemy;


public class MyCollisionListener implements ContactListener {

    World world;

    public MyCollisionListener( World world) {
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object a = fixtureA.getUserData();
        Object b = fixtureB.getUserData();

        if(b instanceof Room.Entrance && a instanceof Player){
            if(!((Room.Entrance) b).isExit()){
                return;
            }
            ((Player) a).setTouchingEntrance(true);

        }
        if(a instanceof Room.Entrance && b instanceof Player){
            if(!((Room.Entrance) a).isExit()){
                return;
            }
            ((Player) b).setTouchingEntrance(true);
        }


        if((b instanceof PlayerAttack)  && a instanceof Enemy){
            if(((Enemy) a).isSpawning()){
                return;
            }

            System.out.println("hit");
            ((Enemy) a).setHit(true);
            if(((Enemy) a).getBody().isActive()) ((Enemy) a).reduceHp(((PlayerAttack) b).getDamage());
        }
        if((a instanceof PlayerAttack)  && b instanceof Enemy){
            if(((Enemy) b).isSpawning()){
                return;
            }

            ((Enemy) b).setHit(true);
            if(((Enemy) b).getBody().isActive()) ((Enemy) b).reduceHp(((PlayerAttack) a).getDamage());

        }






        if((b instanceof Obstacle)  && a instanceof TurretBullet){
            ((TurretBullet) a).setHasToBeDestroyed(true);
        }
        if((a instanceof Obstacle)  && b instanceof TurretBullet){
            System.out.println("reg");
            ((TurretBullet) b).setHasToBeDestroyed(true);
        }

        if((b instanceof Player)  && a instanceof EnemyAttack){
            ((Player) b).getEnemiesAttacks().add((EnemyAttack) a);
            ((Player) b).setGettingHit(true);
            if(a instanceof TurretBullet){
                ((TurretBullet) a).setHasToBeDestroyed(true);
                ((Player) b).reduceHp();
                return;
            }
            ((EnemyAttack) a).enemy.setDoingHit(true);
            ((EnemyAttack) a).reload();
        }

        if((a instanceof Player)  && b instanceof EnemyAttack){
            ((Player) a).getEnemiesAttacks().add((EnemyAttack) b);
            ((Player) a).setGettingHit(true);
            if(b instanceof TurretBullet){
                ((TurretBullet) b).setHasToBeDestroyed(true);
                ((Player) a).reduceHp();
                return;
            }
            ((BaseEnemyAttack) b).enemy.setDoingHit(true);
            ((BaseEnemyAttack) b).reload();

        }


    }


    @Override
    public void endContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object a = fixtureA.getUserData();
        Object b = fixtureB.getUserData();


        if(b instanceof Attack && a instanceof BaseEnemy){

            ((BaseEnemy) a).setHit(false);
        }
        if(a instanceof Attack  && b instanceof BaseEnemy){
            ((BaseEnemy) b).setHit(false);
        }

        if((b instanceof Player)  && a instanceof EnemyAttack){
            ((Player) b).getEnemiesAttacks().removeValue((EnemyAttack) a, false);
            if(a instanceof TurretBullet){
                return;
            }

            ((Player) b).setGettingHit(false);
            ((EnemyAttack) a).enemy.setDoingHit(false);

        }
        if((a instanceof Player)  && b instanceof EnemyAttack){
            ((Player) a).getEnemiesAttacks().removeValue((EnemyAttack) b, false);
            if(b instanceof TurretBullet){
                return;
            }
            ((Player) a).setGettingHit(false);
            ((EnemyAttack) b).enemy.setDoingHit(false);


        }


        if(b instanceof Room.Entrance && a instanceof Player){
            if(!((Room.Entrance) b).isExit()){
                return;
            }

            ((Player) a).setTouchingEntrance(false);

        }
        if(a instanceof Room.Entrance && b instanceof Player){
            if(!((Room.Entrance) a).isExit()){
                return;
            }

            ((Player) b).setTouchingEntrance(false);
        }




    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {


    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public int angleToObstacle(Body body, Obstacle obstacle){
        Vector2 point = new Vector2(
            body.getPosition().cpy().scl(GameSettings.PPM)
        );
        int result;
        int lowX = (int) obstacle.getX();
        int lowY = (int) obstacle.getY();
        int topX = (int) ((int) obstacle.getX() + obstacle.getWidth());
        int topY = (int) ((int) obstacle.getY() + obstacle.getHeight());
        if(point.x < lowX && point.y > lowY){
            result = -90;
        }
        else if(point.x > lowX && point.y > topY){
            result = 180;
        }
        else if(point.x > topX && point.y < topY){
            result = 90;
        }
        else {
            result = 0;
        }
        return result;
    }
}
