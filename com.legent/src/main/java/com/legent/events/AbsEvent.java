package com.legent.events;

/**
 * Created by sylar on 15/8/7.
 */
abstract public class AbsEvent<Pojo> {

    public Pojo pojo;
    public short stoveHeadId;

    public AbsEvent(Pojo pojo) {
        this.pojo = pojo;
    }

    public AbsEvent(Pojo pojo, short stoveHeadId) {
        this.pojo = pojo;
        this.stoveHeadId = stoveHeadId;
    }
}