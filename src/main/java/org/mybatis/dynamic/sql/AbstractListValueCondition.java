/**
 *    Copyright 2016-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.dynamic.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class AbstractListValueCondition<T> implements VisitableCondition<T> {
    private List<T> values = new ArrayList<>();

    protected AbstractListValueCondition(List<T> values) {
        this.values.addAll(values);
    }
    
    public final <R> Stream<R> mapValues(Function<T, R> mapper) {
        return values.stream()
                .map(this::mapValue)
                .map(mapper);
    }
    
    @Override
    public <R> R accept(ConditionVisitor<T, R> visitor) {
        return visitor.visit(this);
    }

    /**
     * This method allows subclasses to alter the value before it is placed
     * into the parameter map.  An example of this is when the case insensitive
     * conditions will change a value to upper case.
     * 
     * <p>We do not expose the values stream because we cannot allow subclasses
     * to change the order or number of values.
     *  
     * @param value the value
     * @return the mapped value - in most cases the value is not changed
     */
    protected T mapValue(T value) {
        return value;
    }
    
    public abstract String renderCondition(String columnName, Stream<String> placeholders);
}
