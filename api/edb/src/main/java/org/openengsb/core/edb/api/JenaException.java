/**
 * Licensed to the Austrian Association for Software Tool Integration (AASTI)
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. The AASTI licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openengsb.core.edb.api;

@SuppressWarnings("serial")
/**
 * This exception is thrown if an error occeured while processing commands at
 * the edb service. Mainly their reason are IOExceptions and JPAExceptions.
 */
public class JenaException extends RuntimeException {
    public JenaException() {
        super();
    }

    public JenaException(String s) {
        super(s);
    }

    public JenaException(Throwable t) {
        super(t);
    }

    public JenaException(String s, Throwable t) {
        super(s, t);
    }
}