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

package org.openengsb.core.persistence.internal;

import org.openengsb.core.api.model.ConfigItem;

/**
 * Internal wrapper around the {@link ConfigItem} to avoid inheritance and similar problems with neodatis.
 */
public class InternalConfigurationItem {

    private ConfigItem<?> configItem;

    public InternalConfigurationItem() {
    }

    public InternalConfigurationItem(ConfigItem<?> configItem) {
        this.configItem = configItem;
    }

    public ConfigItem<?> getConfigItem() {
        return configItem;
    }

    public void setConfigItem(ConfigItem<?> configItem) {
        this.configItem = configItem;
    }

}