package com.argo_entertainment.reactiontime.Actors;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class GameObject extends ModelInstance {

    public Vector3 center = new Vector3();
    public Vector3 dimensions = new Vector3();
    public float radius;

    public BoundingBox bounds = new BoundingBox();
    private String name = "";

    public GameObject (Model model, String rootNode, boolean mergeTransform) {
        super(model, rootNode, mergeTransform);
/*
        Vector3 posit = new Vector3();
        this.transform.getTranslation(posit);
        this.transform.setTranslation(new Vector3(posit.x,posit.y + 1.35f, posit.z));*/

        calculateBoundingBox(bounds);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        bounds.mul(transform);

        dimensions.scl(0, 10f, 0); //@ToDo: Странная херня но именно этот множитель для экспорта из макса

        radius = dimensions.len() / 2f;

    }

    public GameObject (Model model, Vector3 position) {
        super(model, position.x, position.y, position.z);


        calculateBoundingBox(bounds);
        bounds.mul(transform);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);


        //dimensions.scl(0.65f); //@ToDo: Странная херня но именно этот множитель для экспорта из макса

        radius = dimensions.len();
    }

    public GameObject (Model model, String rootNode, boolean mergeTransform, String name) {
        super(model, rootNode, mergeTransform);

        /*Vector3 posit = new Vector3();
        this.transform.getTranslation(posit);
        this.transform.setTranslation(new Vector3(posit.x,posit.y + 1.35f, posit.z));*/

        this.name = name;
        calculateBoundingBox(bounds);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        bounds.mul(transform);
        dimensions.scl(0.65f); //@ToDo: Странная херня но именно этот множитель для экспорта из макса

        radius = dimensions.len() / 2f;
    }

    public BoundingBox getBounds() {
        return bounds;
    }

    public String getName() {
        return nodes.first().id;
    }

    public String getPlanetName(){
        return name;
    }

    public float getRadius() {
        return radius;
    }

    public Vector3 getDimensions() {
        return dimensions;
    }
}
