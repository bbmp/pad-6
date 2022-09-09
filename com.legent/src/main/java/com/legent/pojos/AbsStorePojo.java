package com.legent.pojos;


import com.legent.dao.DaoHelper;

/**
 * Created by sylar on 15/7/7.
 */
abstract public class AbsStorePojo<ID> extends AbsKeyPojo<ID> implements IStorePojo<ID> {

    @Override
    public void save2db() {
        if (DaoHelper.isExists(getClass(), getID())) {
            DaoHelper.update(this);
        } else {
            DaoHelper.create(this);
        }
        DaoHelper.refresh(this);
    }

    @Override
    public void delete(long id) {
        DaoHelper.delete(this);
        DaoHelper.refresh(this);
    }
}
