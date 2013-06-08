/*
 * Copyright (C) 2011-2013 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

/**
 *
 * @author Oleg Tolmatcev
 */
public interface Procedure<T> {
    void apply(T arg) throws Exception;
}
