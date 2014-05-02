/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.windup.addon.config.impl;

import javax.inject.Inject;

import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.windup.addon.config.selectables.Selectable;
import org.jboss.windup.addon.config.selectables.SelectableCondition;
import org.jboss.windup.addon.config.spi.SelectionFactory;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class SelectionFactoryImpl implements SelectionFactory
{
    @Inject
    private AddonRegistry registry;

    @Override
    public <SELECTABLE extends Selectable<CONDITION, SELECTABLE>, CONDITION extends SelectableCondition<SELECTABLE, CONDITION>> CONDITION createQuery(
                Class<SELECTABLE> selectable, String var)
    {
        // TODO support scoping and name references
        SELECTABLE selectableInstance = registry.getServices(selectable).get();
        Class<CONDITION> conditionType = selectableInstance.getSelectableConditionType();
        CONDITION conditionInstance = registry.getServices(conditionType).get();
        return conditionInstance;
    }

    @Override
    public <SELECTABLE extends Selectable<CONDITION, SELECTABLE>, CONDITION extends SelectableCondition<SELECTABLE, CONDITION>> Iterable<SELECTABLE> getQueryResult(
                Class<SELECTABLE> type, String var)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <SELECTABLE extends Selectable<CONDITION, SELECTABLE>, CONDITION extends SelectableCondition<SELECTABLE, CONDITION>> SELECTABLE get(
                Class<SELECTABLE> selectable, String var)
    {
        // TODO support scoping and name references
        SELECTABLE selectableInstance = registry.getServices(selectable).get();
        return selectableInstance;
    }

    @Override
    public <SELECTABLE extends Selectable<CONDITION, SELECTABLE>, CONDITION extends SelectableCondition<SELECTABLE, CONDITION>> SELECTABLE getCurrent(
                Class<SELECTABLE> selectable)
    {
        // TODO support scoping and name references
        SELECTABLE selectableInstance = registry.getServices(selectable).get();
        return selectableInstance;
    }

}
