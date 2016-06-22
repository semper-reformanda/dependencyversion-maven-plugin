/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.reformanda.semper.maven.dependencyversion;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class OmitTransitiveDependenciesTest {

    static Properties properties;

    @BeforeClass
    public static void setUp_beforeClass() throws Exception {
        properties = new Properties();
        properties.load(new FileInputStream("target/test-classes/testPlugin.properties"));
    }

    @Test
    public void shouldFilterRegularDependency() throws Exception {
        assertThat(properties.getProperty("undertow.version"), is(equalTo("1.3.15.Final")));
    }

    @Test
    public void shouldOmitTransitiveDependency() throws Exception {
        assertThat(properties.getProperty("xnio.version"), is(not(equalTo("3.3.4.Final"))));
    }
}
