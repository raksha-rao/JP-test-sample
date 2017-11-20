package com.jptest.payments.fulfillment.testonia.business.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
* Annotation for documenting and validating the immutability of a class and its sub-classes. This can be annotated to
* an interface, in which case all implementing classes and their sub-classes are validated.
* <p/>
* Note : Prefer using {@link javax.annotation.concurrent.Immutable} to this annotation
* 
* @see com.jptest.payments.txnfulfillment.engine.validations.ImmutabilityTest
*/
@Inherited
@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EffectivelyImmutable {

	
}
