/*
 * Copyright (C) 2011-2013 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de>
 */
package jmdicttodsl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Oleg Tolmatcev
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ConverterTest.class,
    DslProcessorTest.class,
    StaxReaderTest.class
})
public class AllTests {
}
