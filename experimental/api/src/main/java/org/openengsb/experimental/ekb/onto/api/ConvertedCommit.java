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

package org.openengsb.experimental.ekb.onto.api;

import java.util.List;

/**
 * A ConvertedCommit class is a helper class. It contains to OntoObjects converted models of an EKBCommit.
 */
public class ConvertedCommit {
    private List<OntoObject> inserts;
    private List<OntoObject> updates;
    private List<OntoObject> deletes;
    
    public List<OntoObject> getInserts() {
        return inserts;
    }
    public void setInserts(List<OntoObject> inserts) {
        this.inserts = inserts;
    }
    public List<OntoObject> getUpdates() {
        return updates;
    }
    public void setUpdates(List<OntoObject> updates) {
        this.updates = updates;
    }
    public List<OntoObject> getDeletes() {
        return deletes;
    }
    public void setDeletes(List<OntoObject> deletes) {
        this.deletes = deletes;
    }
}
