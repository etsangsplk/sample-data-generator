/*
 * Copyright 2014 Open mHealth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openmhealth.data.generator.domain;

import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * A trend wraps a {@link BoundedRandomVariable} in order to provide values whose mean falls along a linear
 * interpolation from a start value to an end value.
 *
 * @author Emerson Farrugia
 */
// TODO refactor this into a container object that wraps a variable and a trend, allowing different variables and
// trend types (linear, polynomial, spline, etc.) with multiple sample points instead of just start and end
public class BoundedRandomVariableTrend {

    private BoundedRandomVariable variable = new BoundedRandomVariable();
    private Double startValue;
    private Double endValue;

    public BoundedRandomVariableTrend() {
    }

    public BoundedRandomVariableTrend(BoundedRandomVariable variable, Double startValue, Double endValue) {

        checkNotNull(variable);
        checkNotNull(startValue);
        checkNotNull(endValue);

        this.variable = variable;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    @NotNull
    public BoundedRandomVariable getVariable() {
        return variable;
    }

    public void setVariable(BoundedRandomVariable variable) {

        checkNotNull(variable);
        this.variable = variable;
    }

    /**
     * @return the value the trend starts with
     */
    @NotNull
    public Double getStartValue() {
        return startValue;
    }

    public void setStartValue(Double startValue) {

        checkNotNull(startValue);
        this.startValue = startValue;
    }

    /**
     * @return the value the trend ends with
     */
    @NotNull
    public Double getEndValue() {
        return endValue;
    }

    public void setEndValue(Double endValue) {

        checkNotNull(endValue);
        this.endValue = endValue;
    }

    /**
     * @param fraction a fraction of the range between the start value and the end value
     * @return the linear interpolation of the value at that fraction
     */
    public Double interpolate(Double fraction) {

        checkArgument(fraction >= 0);
        checkArgument(fraction <= 1);

        return startValue + (endValue - startValue) * fraction;
    }

    /**
     * @param fraction a fraction of the range between the start value and the end value
     * @return a value generated by the bounded random variable when its mean is set to the value of the trend at the
     * given fraction
     */
    public Double nextValue(Double fraction) {

        Double mean = interpolate(fraction);
        return variable.nextValue(mean);
    }

    // TODO remove these setters on a refactor, currently needed by SnakeYaml to support flat structure
    public void setStandardDeviation(Double standardDeviation) {
        variable.setStandardDeviation(standardDeviation);
    }

    public void setMinimumValue(Double minimumValue) {
        variable.setMinimumValue(minimumValue);
    }

    public void setMaximumValue(Double maximumValue) {
        variable.setMaximumValue(maximumValue);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("BoundedRandomVariableTrend{");

        sb.append("variable=").append(variable);
        sb.append(", startValue=").append(startValue);
        sb.append(", endValue=").append(endValue);
        sb.append('}');

        return sb.toString();
    }
}

