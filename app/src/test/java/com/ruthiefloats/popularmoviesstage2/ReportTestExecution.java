package com.ruthiefloats.popularmoviesstage2;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * A Rule
 */
public class ReportTestExecution implements MethodRule {
    /**
     * Modifies the method-running {@link Statement} to implement an additional
     * test-running rule.
     *
     * @param base   The {@link Statement} to be modified
     * @param method The method to be run
     * @param target The object on which the method will be run.
     * @return a new statement, which may be the same as {@code base},
     * a wrapper around {@code base}, or a completely new Statement.
     */
    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, Object target) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.out.println("rule being applied to method: " + method.getName());

                base.evaluate();
                System.out.println("rule done being applied to method: " + method.getName());
            }
        };
    }
}
