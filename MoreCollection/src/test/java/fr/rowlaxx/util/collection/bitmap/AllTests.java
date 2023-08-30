package fr.rowlaxx.util.collection.bitmap;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ 
	AddTest.class, 
	CreationTest.class,
	SerializationTest.class
})
public class AllTests {

}
