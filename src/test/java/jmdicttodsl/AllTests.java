/*
 * Copyright (C) 2011-2014 Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
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
