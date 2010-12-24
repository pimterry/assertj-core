/*
 * Created on Dec 20, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.error.DoesNotContainSequence.doesNotContainSequence;
import static org.fest.assertions.test.DoubleArrayFactory.*;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.unexpectedNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.assertions.util.ArrayWrapperList.wrap;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link DoubleArrays#assertContainsSequence(AssertionInfo, double[], double[])}</code>.
 *
 * @author Alex Ruiz
 */
public class DoubleArrays_assertContainsSequence_Test {

  private static double[] actual;

  @Rule public ExpectedException thrown = none();

  private Failures failures;
  private DoubleArrays arrays;

  @BeforeClass public static void setUpOnce() {
    actual = array(6d, 8d, 10d, 12d);
  }

  @Before public void setUp() {
    failures = spy(Failures.instance());
    arrays = new DoubleArrays(failures);
  }

  @Test public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(unexpectedNull());
    arrays.assertContainsSequence(someInfo(), null, array(8d));
  }

  @Test public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    arrays.assertContainsSequence(someInfo(), actual, null);
  }

  @Test public void should_throw_error_if_sequence_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    arrays.assertContainsSequence(someInfo(), actual, emptyArray());
  }

  @Test public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    double[] sequence = { 6d, 8d, 10d, 12d, 20d, 22d };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenSequenceWasNotFound(info, sequence);
  }

  @Test public void should_fail_if_actual_does_not_contain_whole_sequence() {
    AssertionInfo info = someInfo();
    double[] sequence = { 6d, 20d };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenSequenceWasNotFound(info, sequence);
  }

  @Test public void should_fail_if_actual_contains_first_elements_of_sequence() {
    AssertionInfo info = someInfo();
    double[] sequence = { 6d, 20d, 22d };
    try {
      arrays.assertContainsSequence(info, actual, sequence);
      fail();
    } catch (AssertionError e) {}
    assertThatFailureWasThrownWhenSequenceWasNotFound(info, sequence);
  }

  private void assertThatFailureWasThrownWhenSequenceWasNotFound(AssertionInfo info, double[] sequence) {
    verify(failures).failure(info, doesNotContainSequence(wrap(actual), wrap(sequence)));
  }

  @Test public void should_pass_if_actual_contains_sequence() {
    arrays.assertContainsSequence(someInfo(), actual, array(6d, 8d));
  }

  @Test public void should_pass_if_actual_and_sequence_are_equal() {
    arrays.assertContainsSequence(someInfo(), actual, array(6d, 8d, 10d, 12d));
  }
}
