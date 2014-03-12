/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
package jmdicttodsl;

/**
 *
 * @author Oleg Tolmatcev
 */
public interface Procedure<T> {
    void apply(T arg) throws Exception;
}
