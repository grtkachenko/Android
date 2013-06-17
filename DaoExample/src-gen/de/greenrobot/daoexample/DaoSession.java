package de.greenrobot.daoexample;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.IdentityScopeType;

import de.greenrobot.daoexample.Item;

import de.greenrobot.daoexample.ItemDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig itemDaoConfig;

    private final ItemDao itemDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        itemDaoConfig = daoConfigMap.get(ItemDao.class).clone();
        itemDaoConfig.initIdentityScope(type);

        itemDao = new ItemDao(itemDaoConfig, this);

        registerDao(Item.class, itemDao);
    }
    
    public void clear() {
        itemDaoConfig.getIdentityScope().clear();
    }

    public ItemDao getItemDao() {
        return itemDao;
    }

}